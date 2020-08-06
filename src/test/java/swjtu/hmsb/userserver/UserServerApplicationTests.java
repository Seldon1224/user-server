package swjtu.hmsb.userserver;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import swjtu.hmsb.userserver.dao.User;
import swjtu.hmsb.userserver.milvus.MyMilvus;
import io.milvus.client.*;
import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@SpringBootTest
class UserServerApplicationTests {
	static List<Float> normalizeVector(List<Float> vector) {
		float squareSum = vector.stream().map(x -> x * x).reduce((float) 0, Float::sum);
		final float norm = (float) Math.sqrt(squareSum);
		vector = vector.stream().map(x -> x / norm).collect(Collectors.toList());
		return vector;
	}
	// Helper function that generates random vectors
	static List<List<Float>> generateVectors(long vectorCount, long dimension) {
		SplittableRandom splitcollectionRandom = new SplittableRandom();
		List<List<Float>> vectors = new ArrayList<>();
		for (long i = 0; i < vectorCount; ++i) {
			splitcollectionRandom = splitcollectionRandom.split();
			DoubleStream doubleStream = splitcollectionRandom.doubles(dimension);
			List<Float> vector =
					((DoubleStream) doubleStream).boxed().map(Double::floatValue).collect(Collectors.toList());
			vectors.add(vector);
		}
		return vectors;
	}
	@Test
	void contextLoads() {
/*
		System.out.println("test:");
		MyMilvus myMilvus = new MyMilvus();
		if(!myMilvus.ConnectMilvus())
			System.out.println("connect fail");
		List<Float> vectorList;


		List<List<Float>> vectors = generateVectors(5, 16);

		//List<List<Float>> vectors = generateVectors(vectorCount, dimension);
		vectors =
				vectors.stream().map(UserServerApplicationTests::normalizeVector).collect(Collectors.toList());
		InsertParam insertParam =
				new InsertParam.Builder("hongmo_vectors").withFloatVectors(vectors).build();

		InsertResponse insertResponse = MyMilvus.client.insert(insertParam);
		// Insert returns a list of vector ids that you will be using (if you did not supply them
		// yourself) to reference the vectors you just inserted
		List<Long> vectorIds = insertResponse.getVectorIds();

		// Flush data in collection
		Response flushResponse = MyMilvus.client.flush("hongmo_vectors");
			//System.out.println(id);

*/

		String host = "localhost";
		int port = 19530;
		// Create Milvus client
		MilvusClient client = new MilvusGrpcClient();
		// Connect to Milvus server
		ConnectParam connectParam = new ConnectParam.Builder().withHost(host).withPort(port).build();
		try {
			Response connectResponse = client.connect(connectParam);
		} catch (ConnectFailedException e) {
			System.out.println("Failed to connect to Milvus server: " + e.toString());
		}
		// Check whether we are connected
		boolean connected = client.isConnected();
		ListCollectionsResponse listCollectionsResponse  = client.listCollections();
		System.out.println(listCollectionsResponse.getCollectionNames().toString());
	}

}


