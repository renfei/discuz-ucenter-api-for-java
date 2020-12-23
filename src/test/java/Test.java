import net.renfei.discuz.ucenter.client.Client;
import net.renfei.discuz.ucenter.util.XMLHelper;

import java.util.LinkedList;



/**
 * ================================================
 * Discuz! Ucenter API for JAVA
 * ================================================
 * 测试类
 * 示例：本类实现在如何实现在登入/登出，以及注册。
 *
 * 更多信息：http://code.google.com/p/discuz-ucenter-api-for-java
 * 原作者：梁平 (no_ten@163.com)
 * 创建时间：2009-2-20
 * 更多信息：https://github.com/renfei/discuz-ucenter-api-for-java
 * 修改者：任霏 (i@renfei.net)
 * 修改时间：2020-12-17
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Client uc = new Client("http://localhost/uc_server",null,"123456","3","");
//		testLogin(uc);
//		synlogin(uc);
		String s = uc.ucAuthcode("8485m7QEfsvnOg9tKGvAsxlwXpAzZU6LhFA04pD6N0XIYG1cJVDh2Th83Qcci130UPHbXp+UNG0","DECODE");
		System.out.println(s);
	
	}

	public static void synlogin(Client client){
		String ucsynlogin = client.ucUserSynlogin(1);
		System.out.println("登录成功");
		System.out.println(ucsynlogin);
	}
	
	public static void testLogin(Client client){
		String result = client.ucUserLogin("renfei", "password");
		
		LinkedList<String> rs = XMLHelper.ucUnserialize(result);
		if(rs.size()>0){
			int uid = Integer.parseInt(rs.get(0));
			String username = rs.get(1);
			String password = rs.get(2);
			String email = rs.get(3);
			if(uid > 0) {

				//本地登陆代码
				//TODO ... ....
			} else if(uid == -1) {
				System.out.println("用户不存在,或者被删除");
			} else if(uid == -2) {
				System.out.println("密码错");
			} else {
				System.out.println("未定义");
			}
		}else{
			System.out.println("Login failed");
			System.out.println(result);
		}
	}
	
	public static void testLogout(Client uc){

		//setcookie('Example_auth', '', -86400);
//		生成同步退出的代码
		String ucsynlogout = uc.ucUserSynlogout();
		System.out.println("退出成功"+ucsynlogout);
		

	}
	
	public static void testRegister(Client uc){

		//setcookie('Example_auth', '', -86400);
//		生成同步退出的代码
		String returns = uc.ucUserRegister("cccc", "ccccc" ,"ccc@abc.com" );
		int uid = Integer.parseInt(returns);
		if(uid <= 0) {
			if(uid == -1) {
				System.out.print("用户名不合法");
			} else if(uid == -2) {
				System.out.print("包含要允许注册的词语");
			} else if(uid == -3) {
				System.out.print("用户名已经存在");
			} else if(uid == -4) {
				System.out.print("Email 格式有误");
			} else if(uid == -5) {
				System.out.print("Email 不允许注册");
			} else if(uid == -6) {
				System.out.print("该 Email 已经被注册");
			} else {
				System.out.print("未定义");
			}
		} else {
			System.out.println("OK:"+returns);
		}
		

	}

}
