package com.hrforce;

import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class JobTask {
    private boolean IsRunning=false;

    public void JobOverTime() throws Exception {
        if (!IsRunning)
        {
            IsRunning=true;
            try
            {
                overTime();
            }
            finally
            { IsRunning=false; }
        }
    }

    private static Connection overTime() {

        String driver = SystemProperties.get("master.jdbc.driverClassName");
        String url = SystemProperties.get("master.jdbc.url");
        String username = SystemProperties.get("master.jdbc.username");
        String password = SystemProperties.get("master.jdbc.password");
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName(driver); //classLoader,加载对应驱动
            conn = DriverManager.getConnection(url, username, password);
            stmt = conn.createStatement();

            rs = stmt.executeQuery("select id from t_job_base where status = 0 and isspider=1 order by id desc limit 10");//选择import java.sql.ResultSet;
            while(rs.next()){//如果对象中有数据，就会循环打印出来
                int id=(rs.getInt("id"));
                System.out.println("当前执行ID："+id);
                Statement stmtment = conn.createStatement();
                stmtment.executeUpdate("UPDATE t_job_base set status = 1 where id = "+id);
                ActiveMQ.sendJobMessage("JOB_UPDATE",String.valueOf(id) );
                System.out.println("JOB_UPDATE："+id);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try{
                rs.close();
                stmt.close();
                conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return conn;
    }
}



