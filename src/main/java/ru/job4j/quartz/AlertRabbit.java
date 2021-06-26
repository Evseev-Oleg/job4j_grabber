package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {


    public String readProperties(String way, String key) {
        String res = null;
        Properties property = new Properties();
        try (FileInputStream fis = new FileInputStream(way)) {
            property.load(fis);
            res = property.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    private Connection getConnection(String way) throws SQLException,
            ClassNotFoundException {
        Class.forName(readProperties(way, "driver-class-name"));
        return DriverManager.getConnection(
                readProperties(way, "url"),
                readProperties(way, "username"),
                readProperties(way, "password")
        );
    }

    public static void main(String[] args) {
        String way = "src/main/resources/rabbit.properties";
        AlertRabbit alertRabbit = new AlertRabbit();
        try  (Connection connection = alertRabbit.getConnection(way)) {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDetail job = newJob(Rabbit.class).build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(Integer.parseInt
                            (alertRabbit.readProperties(
                                    way, "rabbit.interval")))
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            Thread.sleep(10000);
            scheduler.shutdown();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException | ClassNotFoundException
                | SQLException | InterruptedException se) {
            se.printStackTrace();
        }
    }

    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            Connection connection = (Connection) context.getJobDetail()
                    .getJobDataMap().get("connection");
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO rabbit (created_date) VALUES (?)")) {
                statement.setDate(1, new Date(System.currentTimeMillis()));
                statement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
