package swjtu.hmsb.userserver.dao;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "hmsb_user")
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    private Long vectorId;
    @Transient
    private String vector;
    public User() {}

    public String getVector() {
        return vector;
    }

    public void setVector(String vector) {
        this.vector = vector;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getVectorId() {
        return vectorId;
    } 

    public void setVectorId(Long vectorId) {
        this.vectorId = vectorId;
    }

    @Override
    public String toString() {
        return name;
    }

    static public List<Float> StringToVector(String vector)
    {
        String[] vectorString = vector.split(",");
        List<Float> vec =  new ArrayList<>();
        for (int i = 0; i < vectorString.length; i++) {
            vec.add(Float.parseFloat(vectorString[i]));
        }
        return vec;
    }
}
