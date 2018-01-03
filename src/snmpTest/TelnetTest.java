package snmpTest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.net.telnet.TelnetClient;

public class TelnetTest {

    private TelnetClient telnetClinet = null;
    private InputStream is = null;
    private OutputStream os = null;
    
    /**
     * 连接远程计算机,连接完成后，获取读取流与发送流
     * @param ip  远程计算机IP地址
     * @param port 远程计算机端口
     * @throws IOException 
     * @throws SocketException 
     * */
    public void connection(String ip,int port) throws SocketException, IOException
    {
           
                    telnetClinet = new TelnetClient();
                    telnetClinet.connect(ip, port);
				
                    is = telnetClinet.getInputStream();
                    os = telnetClinet.getOutputStream();
                    //telnetClinet.
       
    }
    
    /**
     * 获取远程计算机返回的信息
     * */
    public String readTelnetMsg()
    {
            try 
            {
                    int len = 0;
                    byte [] b = new byte[1024];
                    do
                    {
                            len = is.read(b);
                            if(len>=0){
                            	return new String(b,0,len);
                                    
                            }
                         
                    }while(len>=0);
            } 
            catch (IOException e) 
            {
                    e.printStackTrace();
            }
            return null;
    }
    
    /**
     * 向远端计算机发送指令消息
     * @param msg 需要传送的指令
     * **/
    public void sendTelnetMsg(String msg)
    {
            byte [] b = msg.getBytes();
            try 
            {
                    os.write(b, 0, b.length);
                    os.flush();
            } 
            catch (IOException e)
            {
                    e.printStackTrace();
            }
    }
    
    /**
     * 查找远端计算机返回的指令中是否包含想要指令
     * 一直查找，直到包含，返回true
     * */
    public boolean findStr(String str)
    {
            Date time1=new Date();
    		while(((new Date()).getTime()-time1.getTime())<1000)
            {
                    String msg = readTelnetMsg();
                    if(msg.indexOf(str)!=-1)
                            return true;
            }
    		return false;
    }
    
    /**
     * 关闭连接，关闭IO
     * */
    public void close()
    {
            try 
            {
                  
                  telnetClinet.disconnect();
            } 
            catch (IOException e) 
            {
                    e.printStackTrace();
            }
    }
    
    /**
     * 开始测试,连接远程计算机后，发送一个shutdown关机指令
     * @throws ClassNotFoundException 
     * @throws SQLException 
     * @throws InterruptedException 
     * */
    public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException 
    {
            TelnetTest test = new TelnetTest();
            Class.forName("com.mysql.jdbc.Driver");
            String url="jdbc:mysql://10.0.0.41:3306/jeecg";
            String username="admin";
            String password="admin";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		zf_project_info zpi = null;

			conn=DriverManager.getConnection(url,username,password);
			String sql="select zf_name_shi_xian,zf_ip_router,zf_username_router,zf_password_router from zf_project_info";
			ps=conn.prepareStatement(sql);
			rs=ps.executeQuery();
			//rs.next();
			//rs.next();
			while(rs.next()){
				zpi = new zf_project_info();
				zpi.setZf_ip_router(rs.getString("zf_ip_router"));
				zpi.setZf_username_router(rs.getString("zf_username_router"));
				zpi.setZf_password_router(rs.getString("zf_password_router"));
				zpi.setZf_name_shi_xian(rs.getString("zf_name_shi_xian"));

				System.out.println("address is:" + zpi.getZf_ip_router());
				System.out.println("区县的名称是：  " + zpi.getZf_name_shi_xian());
				try {
					test.connection(zpi.getZf_ip_router(), 23);
				} catch (IOException e) {
					System.out.println(zpi.getZf_name_shi_xian()+"无法连接！\n");// TODO Auto-generated catch block
					continue;
				}
				Thread.sleep(1000);
				//if (test.findStr("login")){// 如果远程计算机返回login字符
					test.sendTelnetMsg(zpi.getZf_username_router()+"\n");
				//	System.out.println("login 成功！&&&&&&&");
				//}// 传输用户名,改写你的用户名,该用户名属于TelnetClients组
				Thread.sleep(1000);
				//if (test.findStr("Password")){// 如果远程计算机返回password
					test.sendTelnetMsg("jiesai\n");
				//	System.out.println("password 成功!&&&&&&");
					//}// 传输用户密码,改写你的密码
				Thread.sleep(1000);

				//if (test.findStr(">")){// 如果远程计算机返回>字符
					test.sendTelnetMsg("sys\n");
				//	System.out.println("sys 成功！&&&&&");
				//	}// 发送关机指令
				Thread.sleep(1000);
				
				//if (test.findStr("]")){
					test.sendTelnetMsg("snmp-agent\n");
				//	System.out.println("snmp-agent 成功！&&&&&");
					//}
				Thread.sleep(1000);

				//if (test.findStr("]")){
					test.sendTelnetMsg("snmp-agent community read simple public\n");
				//	System.out.println("snmp-agent community read simple public 成功！\n");
					//}
				Thread.sleep(1000);
				test.sendTelnetMsg("snmp-agent sys-info version all\n");
				//System.out.println("snmp-agent sys-info versin all 成功！&&&&&");
				
				Thread.sleep(1000);
				//if (test.findStr("]")){
					test.sendTelnetMsg("quit\n");
				//	System.out.println("quit-1 成功！&&&&&");
					//}
				Thread.sleep(1000);

				//if (test.findStr(">")){	
					test.sendTelnetMsg("quit\n");
				//	System.out.println("quit-2 成功！&&&&&");
				//if (test.findStr("]")){// 如果远程计算机返回>字符,代表刚才指令发送成功
					test.close();// 断开远程计算机连接
					System.out.println(zpi.getZf_name_shi_xian()+"修改成功了！！\n");
				//}
			}
			
			


            
            
//            test.connection("10.44.229.254", 23);//登录本机,23端口
//            if(test.findStr("login"))//如果远程计算机返回login字符
//                    test.sendTelnetMsg("jiedong\n");//传输用户名,改写你的用户名,该用户名属于TelnetClients组
//            if(test.findStr("Password"))//如果远程计算机返回password
//                    test.sendTelnetMsg("jiesai\n");//传输用户密码,改写你的密码
//            if(test.findStr(">"))//如果远程计算机返回>字符
//                    test.sendTelnetMsg("sys\n");//发送关机指令
//            if(test.findStr("]"))
//                    test.sendTelnetMsg("sysname jy-jd-3600\n");
//            if(test.findStr("]"))//如果远程计算机返回>字符,代表刚才指令发送成功
//                    test.close();//断开远程计算机连接
    }


}
