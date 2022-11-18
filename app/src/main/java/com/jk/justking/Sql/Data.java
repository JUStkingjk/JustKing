package com.jk.justking.Sql;

public class Data {

    public int u_id;

    public String u_name;

    public int age;

    public Data(int id,String name,int age){
        this.u_id = id;
        this.u_name = name;
        this.age = age;
    }

    public Data(){
    }

    @Override
    public String toString() {
        return "Data{" +
                "id=" + u_id +
                ", name='" + u_name + '\'' +
                ", age=" + age +
                '}' + "\n";
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public int getU_id() {
        return u_id;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getU_name() {
        return u_name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
