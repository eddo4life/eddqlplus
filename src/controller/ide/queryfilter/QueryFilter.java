package controller.ide.queryfilter;


import dao.DBMS;

public class QueryFilter {
	
	public QueryFilter(String query) {
		query = queryFilter(query);//filter the query
		//get FiquereOutTheConnectionVariable
		if(!query.isEmpty()) {
		if (DBMS.dbms==1) {//mysql
		new MySQLQueryFilter(query);
		}else if (DBMS.dbms==2) {//oracle
			new OracleQueryFilter(query);
		}
		}
	}
	
	protected String queryFilter(String query) {
		char y[] = query.toCharArray();
		String newString = "";
		int already = 0;
		for (int i = 0; i < query.length(); i++) {
			if (String.valueOf(y[i]).equals(";") || String.valueOf(y[i]).equals(" ")) {
				if (already == 0) {
					newString += y[i];
					already = 1;
				}
			} else {
				already = 0;
				newString += y[i];
			}
		}
		
		
		if (newString.contains(";")) {
			String arr[] = newString.trim().split(";");
			String queryTrim = query.trim();
			int qt = queryTrim.length();
			if (queryTrim.substring(qt - 1, qt).equals(";"))
				return arr[arr.length - 1] + ";";
		}
		return "";
	}

}
