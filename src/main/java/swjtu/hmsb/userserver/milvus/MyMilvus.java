package swjtu.hmsb.userserver.milvus;

import com.google.gson.JsonObject;
import io.milvus.client.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//use Milvus Java SDK
public class MyMilvus {
    String host = "localhost";
    int port = 19530;
    final String collectionName = "hongmo_vectors"; // collection name
    final long dimension = 8; // dimension of each vector
    final long indexFileSize = 1024; // maximum size (in MB) of each index file
    final MetricType metricType = MetricType.IP;
    final int topK = 2;
    MilvusClient client;



    public void ConnectMilvus(){
        // Create Milvus client
        client = new MilvusGrpcClient();
        // Connect to Milvus server
        ConnectParam connectParam = new ConnectParam.Builder().withHost(host).withPort(port).build();
        try {
            Response connectResponse = client.connect(connectParam);
        } catch (ConnectFailedException e) {
            System.out.println("Failed to connect to Milvus server: " + e.toString());
        }
        // Check whether we are connected
        boolean connected = client.isConnected();
        // Create a collection with the following collection mapping
        // we choose IP (Inner Product) as our metric type
        CollectionMapping collectionMapping =
                new CollectionMapping.Builder(collectionName, dimension)
                        .withIndexFileSize(indexFileSize)
                        .withMetricType(metricType)
                        .build();
        Response createCollectionResponse = client.createCollection(collectionMapping);
        // Check whether the collection exists
        HasCollectionResponse hasCollectionResponse = client.hasCollection(collectionName);
        // Get collection info
        GetCollectionInfoResponse getCollectionInfoResponse = client.getCollectionInfo(collectionName);
    }

    //return insert vector id
    //public Long InsertVector(List<Float> vector) {
    public Long InsertVector(List<List<Float>> vectors) {

        vectors =
                vectors.stream().map(MyMilvus::normalizeVector).collect(Collectors.toList());
        InsertParam insertParam =
                new InsertParam.Builder(collectionName).withFloatVectors(vectors).build();
        InsertResponse insertResponse = client.insert(insertParam);
        // Insert returns a list of vector ids that you will be using (if you did not supply them
        // yourself) to reference the vectors you just inserted
        Long vectorId = insertResponse.getVectorIds().get(0);
        System.out.println(vectorId);
        // Flush data in collection
        Response flushResponse = client.flush(collectionName);
        // Create index for the collection
        // We choose IVF_SQ8 as our index type here. Refer to IndexType javadoc for a
        // complete explanation of different index types
        final IndexType indexType = IndexType.IVF_SQ8;
        // Each index type has its optional parameters you can set. Refer to the Milvus documentation
        // for how to set the optimal parameters based on your needs.
        JsonObject indexParamsJson = new JsonObject();
        indexParamsJson.addProperty("nlist", 16384);
        Index index =
                new Index.Builder(collectionName, indexType)
                        .withParamsInJson(indexParamsJson.toString())
                        .build();
        Response createIndexResponse = client.createIndex(index);
        return vectorId;
    }

    //return the most similar vector's id
    public Long SearchVector(List<List<Float>>  vectors){
        JsonObject searchParamsJson = new JsonObject();

        searchParamsJson.addProperty("nprobe", 20);
        vectors =
                vectors.stream().map(MyMilvus::normalizeVector).collect(Collectors.toList());

        SearchParam searchParam =
                new SearchParam.Builder(collectionName)
                .withFloatVectors(vectors)
                .withTopK(topK)
                .withParamsInJson(searchParamsJson.toString())
                .build();
        SearchResponse searchResponse = client.search(searchParam);
        List<List<Long>> resultIds = searchResponse.getResultIdsList();
        List<List<Float>> resultDistances = searchResponse.getResultDistancesList();

        System.out.println(searchResponse);
        if(resultIds == null)
            return 0L;
        else
            return resultIds.get(0).get(0);
    }

    public void DropConnect(){
        // Drop index for the collection
        Response dropIndexResponse = client.dropIndex(collectionName);

        // Drop collection
        Response dropCollectionResponse = client.dropCollection(collectionName);

        // Disconnect from Milvus server
        try {
            Response disconnectResponse = client.disconnect();
        } catch (InterruptedException e) {
            System.out.println("Failed to disconnect: " + e.toString());
        }
    }

    public static List<Float> normalizeVector(List<Float> vector) {
        float squareSum = vector.stream().map(x -> x * x).reduce((float) 0, Float::sum);
        final float norm = (float) Math.sqrt(squareSum);
        vector = vector.stream().map(x -> x / norm).collect(Collectors.toList());
        return vector;
    }
}
