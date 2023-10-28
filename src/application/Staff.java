package application;
public class Staff extends Person{ //TODO make this generic so Staff and Student can inherit
	int inCharge; //-1 if not In Char, otherwise put camp.id
	
	public Staff(String name, String id, String faculty,String password) {
		super();
		this.fillDetails(name, id, faculty, password);
		this.inCharge = -1;
	}


}

