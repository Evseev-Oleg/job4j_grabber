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


    private Properties readProperties() {
        Properties property = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/rabbit.properties")) {
            property.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return property;
    }

    private Connection getConnection(Properties properties) throws SQLException,
            ClassNotFoundException {
        Class.forName(properties.getProperty("driver-class-name"));
        return DriverManager.getConnection(
                properties.getProperty("url"),
                properties.getProperty("username"),
                properties.getProperty("password")
        );
    }

    public static void main(String[] args) {
        AlertRabbit alertRabbit = new AlertRabbit();
        Properties properties = alertRabbit.readProperties();
        try (Connection connection = alertRabbit.getConnection(properties)) {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("connection", connection);
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(Integer.parseInt(
                            properties.getProperty(
                                    "rabbit.interval")))
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
