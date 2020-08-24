package swjtu.hmsb.userserver.dao;

import com.sun.org.apache.xpath.internal.objects.XString;

import java.util.ArrayList;
import java.util.Date;
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
    private String sex;
    private String phone;
    private String birthday;
    private String email;
    private Long vectorId;
    @Transient
    private String vector;
    public User() { }
    public User(Long id, String sex, String phone, String birthday, String email) {
        this.id = id;
        this.sex = sex;
        this.phone = phone;
        this.birthday = birthday;
        this.email = email;
    }

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
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", phone='" + phone + '\'' +
                ", birthday='" + birthday + '\'' +
                ", email='" + email + '\'' +
                ", vectorId=" + vectorId +
                ", vector='" + vector + '\'' +
                '}';
    }

    //    @Override
//    public String toString() {
//        return name;
//    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getSex() {
        return sex;
    }

    public String getPhone() {
        return phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getEmail() {
        return email;
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
