
// $Id$

package net.sf.persist.tests.performance;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.persist.Persist;
import net.sf.persist.tests.framework.ConnectionHelper;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

public class PerformanceH2 {
	
	public static AllTypes buildAllTypes() {
		AllTypes a = new AllTypes();
		byte[] binaryCol = new byte[255]; for (int i=0; i<255; i++) binaryCol[i]=(byte)(i/2);
		a.setBinaryCol(binaryCol);
		a.setBlobCol(new byte[] {1,2,3,4,5,6,7,8,9,10});
		Map obj = new HashMap(); obj.put("x", "y");
		a.setOtherCol(obj);
		a.setDateCol(new java.sql.Date(System.currentTimeMillis()));
		a.setTimeCol(new java.sql.Time(System.currentTimeMillis()));
		a.setTimestampCol(new java.sql.Timestamp(System.currentTimeMillis()));
		a.setCharCol("hello world char");
		a.setVarcharCol("hello world varchar");
		a.setVarcharIgnorecaseCol("hello world varchar ignore case");
		a.setClobCol("hello world clob");
		a.setIntCol(12345678);
		a.setBooleanCol(true);
		a.setTinyintCol(123);
		a.setSmallintCol(123);
		a.setBigintCol(Long.MAX_VALUE/2);
		a.setDecimalCol(12345678);
		a.setDoubleCol(1234.5678);
		a.setRealCol(1234.56f);
		return a;
	}

	public static void main(String[] args) throws SQLException, IOException {
		
		String reportPath = args[0];
		
		String properties = "net/sf/persist/tests/performance/h2.properties";
		
		// create a connection
		Connection connection = ConnectionHelper.getConnection(properties);
		connection.setAutoCommit(true);
		connection.createStatement().execute("create table if not exists all_types ( int_col int, boolean_col boolean, tinyint_col tinyint, smallint_col smallint, bigint_col bigint, decimal_col decimal, double_col double, real_col real, time_col time, date_col date, timestamp_col timestamp, binary_col binary, blob_col blob, other_col other, uuid_col uuid, varchar_col varchar, varchar_ignorecase_col varchar_ignorecase, char_col char, clob_col clob, id int auto_increment )");
		connection.createStatement().execute("delete from all_types");
		
		// persist
		Persist persist = new Persist(connection);		
		
		int inserts = 5000;
		int reads = 10000;
		int readLists = 5;
		
		AllTypes a = buildAllTypes();
		
		// insert
		
		System.out.print("insert");

		String sql = "insert into ALL_TYPES(INT_COL,BOOLEAN_COL,TINYINT_COL,SMALLINT_COL,BIGINT_COL,DECIMAL_COL," +
		"DOUBLE_COL,REAL_COL,TIME_COL,DATE_COL,TIMESTAMP_COL,BINARY_COL,BLOB_COL,OTHER_COL,UUID_COL," +
		"VARCHAR_COL,VARCHAR_IGNORECASE_COL,CHAR_COL,CLOB_COL)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		for (int i=1; i<=inserts; i++) {
			
			if (i%10==0) System.out.print(" " + i);
			if (i%250==0) System.out.println();
			
			// jdbc
			Monitor mon = MonitorFactory.start("[insert] jdbc");
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setLong(1,a.getIntCol());
			stmt.setBoolean(2,a.getBooleanCol());
			stmt.setInt(3,a.getTinyintCol());
			stmt.setInt(4,a.getSmallintCol());
			stmt.setLong(5,a.getBigintCol());
			stmt.setLong(6,a.getDecimalCol());
			stmt.setDouble(7,a.getDoubleCol());
			stmt.setFloat(8,a.getRealCol());
			stmt.setTime(9,a.getTimeCol());
			stmt.setDate(10,a.getDateCol());
			stmt.setTimestamp(11,a.getTimestampCol());
			stmt.setBytes(12,a.getBinaryCol());
			stmt.setBytes(13,a.getBlobCol());
			stmt.setObject(14,a.getOtherCol());
			stmt.setBytes(15,a.getUuidCol());
			stmt.setString(16,a.getVarcharCol());
			stmt.setString(17,a.getVarcharIgnorecaseCol());
			stmt.setString(18,a.getCharCol());
			stmt.setString(19,a.getClobCol());
			stmt.executeUpdate();
			stmt.close();
			mon.stop();
			
			// persist
			mon = MonitorFactory.start("[insert] persist");
			persist.insert(a);
			mon.stop();
			
		}
		System.out.println();
		
		
		// find a single id to be used by select by primary key
		long id = persist.read(int.class, "select min(id) from all_types");
		
		// read
		
		System.out.print("read");
		
		a = new AllTypes();
		
		for (int i=1; i<=reads; i++) {
			if (i%50==0) System.out.print(" " + i);
			if (i%1000==0) System.out.println();
			
			// jdbc
			Monitor mon = MonitorFactory.start("[read single] jdbc");
			PreparedStatement stmt = connection.prepareStatement("select * from all_types where id=?");
			stmt.setLong(1, id);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			a.setIntCol(rs.getLong(1));
			a.setBooleanCol(rs.getBoolean(2));
			a.setTinyintCol(rs.getInt(3));
			a.setSmallintCol(rs.getInt(4));
			a.setBigintCol(rs.getLong(5));
			a.setDecimalCol(rs.getLong(6));
			a.setDoubleCol(rs.getDouble(7));
			a.setRealCol(rs.getFloat(8));
			a.setTimeCol(rs.getTime(9));
			a.setDateCol(rs.getDate(10));
			a.setTimestampCol(rs.getTimestamp(11));
			a.setBinaryCol(rs.getBytes(12));
			a.setBlobCol(rs.getBytes(13));
			a.setOtherCol(rs.getObject(14));
			a.setUuidCol(rs.getBytes(15));
			a.setVarcharCol(rs.getString(16));
			a.setVarcharIgnorecaseCol(rs.getString(17));
			a.setCharCol(rs.getString(18));
			a.setClobCol(rs.getString(19));
			stmt.close();
			mon.stop();
			
			mon = MonitorFactory.start("[read single] persist");
			a = persist.readByPrimaryKey(AllTypes.class, id);
			mon.stop();
			
			mon = MonitorFactory.start("[read single] persist map");
			/*Map<String, Object> m =*/ persist.readMap("select * from all_types where id=?", id);
			mon.stop();
		}
		System.out.println();
		
		System.out.print("read list");
		for (int i=1; i<=readLists; i++) {
			if (i%1==0) System.out.print(" " + i);
			if (i%20==0) System.out.println();
			
			List al = new ArrayList();
			Monitor mon = MonitorFactory.start("[read list] jdbc");
			PreparedStatement stmt = connection.prepareStatement("select * from all_types");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				a.setIntCol(rs.getLong(1));
				a.setBooleanCol(rs.getBoolean(2));
				a.setTinyintCol(rs.getInt(3));
				a.setSmallintCol(rs.getInt(4));
				a.setBigintCol(rs.getLong(5));
				a.setDecimalCol(rs.getLong(6));
				a.setDoubleCol(rs.getDouble(7));
				a.setRealCol(rs.getFloat(8));
				a.setTimeCol(rs.getTime(9));
				a.setDateCol(rs.getDate(10));
				a.setTimestampCol(rs.getTimestamp(11));
				a.setBinaryCol(rs.getBytes(12));
				a.setBlobCol(rs.getBytes(13));
				a.setOtherCol(rs.getObject(14));
				a.setUuidCol(rs.getBytes(15));
				a.setVarcharCol(rs.getString(16));
				a.setVarcharIgnorecaseCol(rs.getString(17));
				a.setCharCol(rs.getString(18));
				a.setClobCol(rs.getString(19));
				al.add(a);
			}
			stmt.close();
			mon.stop();

			mon = MonitorFactory.start("[read list] persist");
			/*List<AllTypes> list =*/ persist.readList(AllTypes.class);
			mon.stop();
			
			mon = MonitorFactory.start("[read list] persist map");
			/*List<Map<String,Object>> map =*/ persist.readMapList("select * from all_types");
			mon.stop();
		}
		System.out.println();
		
		System.out.println();
		System.out.println("finished");
		
		// read sort table javascript
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("net/sf/persist/tests/performance/sorttable.js");
		BufferedReader r = new BufferedReader(new InputStreamReader(is));
		StringBuffer sorttable = new StringBuffer();
		String line;
		while ((line = r.readLine())!=null) {
			sorttable.append(line+"\n");
		}
		
		// create report html
		System.out.println("writing report to " + reportPath);
		String report = MonitorFactory.getReport();
		Writer w = new BufferedWriter(new FileWriter(reportPath));
		w.write("<html><head><style>*{font-family:tahoma; font-size:8pt} td{padding:3px}</style>");
		w.write("<script>\n" + sorttable + "</script>");
		w.write("</head><body onload=\"initTable('report-table')\">");
		w.write(report.replace("<table", "<table id=\"report-table\""));
		w.write("</body></html>");
		w.close();
		
	}

}
