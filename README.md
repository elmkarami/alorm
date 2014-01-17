### Alorm (Android Lite ORM)
![Alorm](http://img15.hostingpics.net/thumbs/mini_276571alorm.jpg)

***
In mobile development with the Android platform, the interaction with the database proves the most difficult thing to maintain.
In addition, on the Android platform, the number of open source Framework in the service very limited and they usually do not cover all the features
From here comes the idea of ​​creating a Framework which will aim to cover all conventional operations such as creating tables, the backup, update and deletion.
The Framework (ALORM) guarantee the creation of tables respecting the integrity constraints from the annotated classes.
The Framework also provides an interface simplifies writing conventional operations of data manipulation (CRUD). 

## 1. Introduction

Alorm is an open source framework for managing persistent objects in a relational database on the Android platform, to work with ALORM, the first thing to do is download the latest release from here: https://github.com/elmehdikarami/alorm/releases

Then it must be integrated into the "Build Path" from your Android application, this can be done by two methods: 

         1. The first is to add the jar to the 'libs' directory. 
         2. The second is to add the jar directly in the build path by following the steps below (case of Eclipse) : 
            * Right click on your project 
            * Select Build Path> Configure Build Path 
            * Click Add External Jars and browse to the jar, and then click OK.

## 2. Configuring DataBase

To specify to Alorm all informations about the database (name, type, version: in the case of SQLite), create a configuration file named 'alorm.properties' in the root folder 'assets'. Here's an example of 'alorm.properties': 
```
dbName=db.sqlite                        #(database name)
dbType=sqlite                           #(database type)
dbVersion=1                             #(database version)
```

## 3. First application with Alorm

After creating an Android project (or use an existing project), integrate and create Alorm alorm.properties file defining the parameters of your database, we will see how to define classes of models. Before starting the examples, the table below includes the annotations provided by the Framework and what they were build for :


<table>
  <tr>
    <th>Annotation</th>
    <th>Attribute</th>
    <th>Default</th>
    <th>Explanation</th>
  </tr>
  <tr>
    <td>Entity</td>
    <td>table</td>
    <td>Class's name</td>
    <td> Name of the table</td>
  </tr>
  <tr>
    <td rowspan="8">Collumn</td>
    <td>name</td>
    <td>attribute's name</td>
    <td>Specify the column's name</td>
  </tr>
  <tr>
    <td>primarykey</td>
    <td>false</td>
    <td>Sets the primary key constraint on a field</td>
  </tr>
  <tr>
    <td>autoincrement</td>
    <td>false</td>
    <td>Defines the constraint auto_increment on a field</td>
  </tr>
  <tr>
    <td>notnull</td>
    <td>false</td>
    <td>Sets the constraint 'Not Null' on a field</td>
  </tr>
  <tr>
    <td>default</td>
    <td>NULL</td>
    <td>Specify a default value to a field</td>
  </tr>
  <tr>
    <td>attribute_ref</td>
    <td>NULL</td>
    <td>Indicates which list an object belongs</td>
  </tr>
  <tr>
    <td>oncascade</td>
    <td>false</td>
    <td>Set the action to perform</td>
  </tr>
    <tr>
    <td>lazy</td>
    <td>true</td>
    <td>activate or not the lazy loading</td>
  </tr>
</table>

## 4. Examples
**Main rule: Each entity must have a default constructor and an ID, and each attribute must have a getter and setter.**

**The database schema is created automatically using the reflection.**

These examples show how to use Alorm in several use cases ! 
### Example 1 : 
First define our Car class: 
```java
@Entity(table = "carTable") //Indicate that it is an entity + table name
public class Car {
        @Column(primarykey = true)  //Define a primary key
        private Integer number;
        private int modele;
        private String company;
        // getters & setters
```
To interact with the database, just get the Session and call the appropriate methods. Here's an example for saving a car: 
```java
        Car car = new Car ();
        car.setNumber("8754");
        car.setCompany("Alorm");
        car.setModele(2014);
        try{
        // context references the current Activity
         Session session = SessionFactory.getSession(context);//get the session using the factory
          session.open();                                    // open the connection
          session.beginTransaction(); //begin a transaction
          session.save(car); //save the entity
          session.commit();//commit
          session.close(); //close the connection
        }catch(AlormException e){  
          session.rollBack();   
                }
```
For other methods, here are some examples of how to use them in a transaction: modify an object: 
```java
   car.setCompany("Mercedes");
   session.update(car);
```
Delete an object : 
```java
session.deleteByPK(Car.class,"8754");
```
Find an object using its primary key: 
```java
Car car = session.findByPK(Car.class, "8754");
```
Get a list: Return all registered objects : 
```java
List<Car> carList = session.getAll(Car.class) ;
```
To add one or more conditions to the selection, simply proceed as follows: 
```java
List<Car> carList = session.getAll(Car.class, "company = ? and modele = ?", "Mercedes",2014) ;
```
### Example 2 : Relation One-to-One
In this example we see the relationship 1-1 (bidirectional and unidirectional): Each person has a car, and each car belongs to one person 
```java
@Entity
public class Car {
        @Column(primarykey = true)
        private Integer number;
        private int modele;
        private String company;
        @Column(onCascade = true)
        private Person person;
        // getters & setters
}
```
```java
@Entity
public class Person {
        @Column(primarykey = true)
        private Integer id;
        private String firstName;
        private String lastName;
        private Car car;
        //getters & setters
}
```
<b>In this case we have a bidirectional relationship, to make it unidictional, simply remove the arrribute person or car.</b>
* Cascade : Allows you to add, modify and delete object(Collection) in cascade, just add the (onCascade = true) annotation on the attribute (Collection) desired. 

### Example 3 : Ralation One-to-Many :
Continuing the previous example, and assume that a person can have several cars. So just replace the attribute
```java
    private Car car;
```
    By
```java
    private List<Car> cars;
```
<b>Lazy Loading</b> : To disable the lazy loading on an attribute (object/collection) just proceed as below : 
```java
@Column(lazy = false)
private List<Car> cars
```
<b>Several lists</b> : One of the cases that may be possible is to have two or more collections of the same class : <br/>
<b>1. Unidirectional : </b>
```java
@Entity
public class Person{
  @Column(primarykey = true)
  private Integer id;
  private String firstName;
  private String lastName;
  private List<Car> forSale
  private List<Car> forRent;
//getters & setters
}
```
<b>2. Bidirectional : </b> To make it bidirectional, just add the annotation @Column(attribut_ref = "forSale") to the attribute 
```java
@Entity
public class Car {
...
  @Column(attribute_ref="forSale")
  private Person person;
        // getters & setters
}
```
<b>This solution can also cover the case of multiple objects</b>
### Example 4 : Relation Many-to-Many :<br/>
Continuing the previous example, suppose that each person can have one or more cars, and each car belongs to one or more persons in this case each class must have a collection of the other class. 
```java
@Entity
public class Car {
  private List<Person> persones
// getters & setters + attributs
}
```
```java
@Entity
public class Person {
  private List<Car> cars;
//getters & setters + attributs
}
```
### Example 5 : Inheritance : <br/>
Alorm support also the notion of inheritance. Each class has its own table, so that the specific attributes of the subclass can have the Not Null constraint 
```java
@Entity
public class Person {
  @Column(primarykey = true)
  private Integer idP;
// getters & setters
}


@Entity
public class Student extends Person 
  @Column(primarykey = true)
  private Integer idE;
  private String name;
// getters & setters + attributs
}
```
### Example 6 : Custom queries : <br/>
Alorm allows the developers to run custom queries, leaving to the developer the possibiliy to execute native SQL query : 
```java
Cursor cursor =  session.querySql(String "select * from Car where modele = ?","2014"); // You can add conditions as much as you want
session.execSql("delete from Car where modele = 2014");
```

### Example 7 : AlormTemplate : <br/>
This concept comes to simplify the use of the session, for example if you want to do a single operation (add, modify, delete), then you must open a session, start a transaction, commit and close the session.<br/>
To avoid this repetition we thought to introduce the notion of template 
```java
AlormTemplate template = AlormTemplate.getTemplate(conext) ;
template.save(car); //After calling of save, the car object is already saved !
```
