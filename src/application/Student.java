package application;
public class Student extends User{ 
	int committee; //-1 if not committee member, otherwise put camp.id
	
	public Student(String name, String id, String faculty,String password) {
		super();
		this.fillDetails(name, id, faculty, password);
		this.committee = -1;		
	}


}
