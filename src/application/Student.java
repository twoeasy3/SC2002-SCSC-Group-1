package application;
public class Student extends Person{ 
	int committee; //-1 if not committee member, otherwise put camp.id
	
	public Student(String name, String id, String faculty,String password) {
		super();
		this.fillDetails(name, id, faculty, password);
		this.committee = -1;		
	}


}
