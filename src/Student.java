
class Student {
	String name;
	String id;
	String faculty;
	String password;
	int committee; //-1 if not committee member, otherwise put camp.id
	
	public Student(String name, String id, String faculty) {
		this.name = name;
		this.id = id;
		this.faculty = faculty;
		this.password = "password";
		this.committee = -1;
	}
	
	public void changePass(String lastpass, String newpass){
		if(lastpass == this.password) {
			this.password = newpass;
			System.out.println("Password successfully updated.");
		}
		else {
			System.out.println("Current password is incorrect.");
		}
	}
		

}
