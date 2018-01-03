package snmpTest;

public class zf_project_info {

	private String zf_ip_router;
	private String zf_username_router;
	private String zf_password_router;
	private String zf_name_shi_xian;
	
	public String getZf_name_shi_xian() {
		return zf_name_shi_xian;
	}
	public void setZf_name_shi_xian(String zf_name_shi_xian) {
		this.zf_name_shi_xian = zf_name_shi_xian;
	}
	public String getZf_ip_router() {
		return zf_ip_router;
	}
	public void setZf_ip_router(String zf_ip_router) {
		this.zf_ip_router = zf_ip_router;
	}
	public String getZf_username_router() {
		return zf_username_router;
	}
	public void setZf_username_router(String zf_username_router) {
		this.zf_username_router = zf_username_router;
	}
	public String getZf_password_router() {
		return zf_password_router;
	}
	public void setZf_password_router(String zf_password_router) {
		this.zf_password_router = zf_password_router;
	}
	public zf_project_info() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "zf_project_info [zf_ip_router=" + zf_ip_router + ", zf_username_router=" + zf_username_router
				+ ", zf_password_router=" + zf_password_router + "]";
	}

	
}
