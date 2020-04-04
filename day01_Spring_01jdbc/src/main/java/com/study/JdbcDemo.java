package com.study;

import java.sql.*;

/**
 * 程序的耦合:
 *      耦合：程序间的依赖关系
 *          包括：类之间的依赖，方法之间的依赖
*       解藕：降低程序间的依赖关系
 *      实际开发时：应该做到编译时不依赖，运行时才依赖。
 * 解藕的思路：
 *  1、使用反射来创建对象，而避免使用new创建对象。前者依赖的是forName中的参数字符串，后者依赖的是具体的类
 *  2、通过读取配置文件来出获取要创建的全限定类名
 *
 */
public class JdbcDemo {
    public static void main(String[] args) throws Exception {
        // 1、注册驱动
        //DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        Class.forName("com.mysql.jdbc.Driver"); //采用反射，若mysql的jar包不存在，仅在运行时才会出异常，编译时不会报错
        // 2、获取连接
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/eesy?useUnicode=true&characterEncoding=utf-8", "root", "12345678");
        // 3、获取操作数据库的预处理对象
        PreparedStatement pstm = con.prepareStatement("select  * from account");
        // 4、执行SQL语句，得到结果集
        ResultSet rs = pstm.executeQuery();
        // 5、遍历（封装）结果集
        while (rs.next()){
            System.out.println(rs.getString("name"));
        }
        // 6、释放资源(先开的最后关)
        rs.close();
        pstm.close();
        con.close();
    }
}
