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
     * ����Զ�̼����,������ɺ󣬻�ȡ��ȡ���뷢����
     * @param ip  Զ�̼����IP��ַ
     * @param port Զ�̼�����˿�
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
     * ��ȡԶ�̼�������ص���Ϣ
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
     * ��Զ�˼��������ָ����Ϣ
     * @param msg ��Ҫ���͵�ָ��
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
     * ����Զ�˼�������ص�ָ�����Ƿ������Ҫָ��
     * һֱ���ң�ֱ������������true
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
     * �ر����ӣ��ر�IO
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
     * ��ʼ����,����Զ�̼�����󣬷���һ��shutdown�ػ�ָ��
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
				System.out.println("���ص������ǣ�  " + zpi.getZf_name_shi_xian());
				try {
					test.connection(zpi.getZf_ip_router(), 23);
				} catch (IOException e) {
					System.out.println(zpi.getZf_name_shi_xian()+"�޷����ӣ�\n");// TODO Auto-generated catch block
					continue;
				}
				Thread.sleep(1000);
				//if (test.findStr("login")){// ���Զ�̼��������login�ַ�
					test.sendTelnetMsg(zpi.getZf_username_router()+"\n");
				//	System.out.println("login �ɹ���&&&&&&&");
				//}// �����û���,��д����û���,���û�������TelnetClients��
				Thread.sleep(1000);
				//if (test.findStr("Password")){// ���Զ�̼��������password
					test.sendTelnetMsg("jiesai\n");
				//	System.out.println("password �ɹ�!&&&&&&");
					//}// �����û�����,��д�������
				Thread.sleep(1000);

				//if (test.findStr(">")){// ���Զ�̼��������>�ַ�
					test.sendTelnetMsg("sys\n");
				//	System.out.println("sys �ɹ���&&&&&");
				//	}// ���͹ػ�ָ��
				Thread.sleep(1000);
				
				//if (test.findStr("]")){
					test.sendTelnetMsg("snmp-agent\n");
				//	System.out.println("snmp-agent �ɹ���&&&&&");
					//}
				Thread.sleep(1000);

				//if (test.findStr("]")){
					test.sendTelnetMsg("snmp-agent community read simple public\n");
				//	System.out.println("snmp-agent community read simple public �ɹ���\n");
					//}
				Thread.sleep(1000);
				test.sendTelnetMsg("snmp-agent sys-info version all\n");
				//System.out.println("snmp-agent sys-info versin all �ɹ���&&&&&");
				
				Thread.sleep(1000);
				//if (test.findStr("]")){
					test.sendTelnetMsg("quit\n");
				//	System.out.println("quit-1 �ɹ���&&&&&");
					//}
				Thread.sleep(1000);

				//if (test.findStr(">")){	
					test.sendTelnetMsg("quit\n");
				//	System.out.println("quit-2 �ɹ���&&&&&");
				//if (test.findStr("]")){// ���Զ�̼��������>�ַ�,����ղ�ָ��ͳɹ�
					test.close();// �Ͽ�Զ�̼��������
					System.out.println(zpi.getZf_name_shi_xian()+"�޸ĳɹ��ˣ���\n");
				//}
			}
			
			


            
            
//            test.connection("10.44.229.254", 23);//��¼����,23�˿�
//            if(test.findStr("login"))//���Զ�̼��������login�ַ�
//                    test.sendTelnetMsg("jiedong\n");//�����û���,��д����û���,���û�������TelnetClients��
//            if(test.findStr("Password"))//���Զ�̼��������password
//                    test.sendTelnetMsg("jiesai\n");//�����û�����,��д�������
//            if(test.findStr(">"))//���Զ�̼��������>�ַ�
//                    test.sendTelnetMsg("sys\n");//���͹ػ�ָ��
//            if(test.findStr("]"))
//                    test.sendTelnetMsg("sysname jy-jd-3600\n");
//            if(test.findStr("]"))//���Զ�̼��������>�ַ�,����ղ�ָ��ͳɹ�
//                    test.close();//�Ͽ�Զ�̼��������
    }


}
