//package com.bridgelabz.app;
//
//import com.bridgelabz.controller.QuantityMeasurementController;
//import com.bridgelabz.entity.QuantityMeasurementEntity;
//import com.bridgelabz.dto.QuantityDTO;
//import com.bridgelabz.dump.repository.IQuantityMeasurementRepository;
//import com.bridgelabz.dump.repository.QuantityMeasurementCacheRepository;
//import com.bridgelabz.dump.repository.QuantityMeasurementDatabaseRepository;
//import com.bridgelabz.service.IQuantityMeasurementService;
//import com.bridgelabz.service.QuantityMeasurementServiceImpl;
//import com.bridgelabz.util.ApplicationConfig;
//import com.bridgelabz.util.ConnectionPool;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//
//public class QuantityMeasurementApp {
//
//    public static void main(String[] args) {
//
//        String repoType = ApplicationConfig.getProperty("repository.type");
//        System.out.println("Repository Type: " + repoType);
//        IQuantityMeasurementRepository repository;
//
//        if ("database".equalsIgnoreCase(repoType)) {
//            repository = new QuantityMeasurementDatabaseRepository();
//            System.out.println("Using Database Repository");
//        } else {
//            repository = QuantityMeasurementCacheRepository.getInstance();
//            System.out.println("Using Cache Repository");
//        }
//
//        IQuantityMeasurementService service =
//                new QuantityMeasurementServiceImpl(repository);
//
//        QuantityMeasurementController controller =
//                new QuantityMeasurementController(service);
//
//
//        QuantityDTO q1 = new QuantityDTO(5,"FEET");
//        QuantityDTO q2 = new QuantityDTO(15,"FEET");
//
//        QuantityDTO result = controller.performAddition(q1,q2);
//
//        System.out.println("Addition Result: " + result.getValue());
//
//    }
//
//    public void save(QuantityMeasurementEntity entity) {
//
//        System.out.println("Saving data to database...");
//
//        String sql = "INSERT INTO quantity_measurement_entity "
//                + "(operation, measurement_type, value1, value2, result) "
//                + "VALUES (?, ?, ?, ?, ?)";
//
//        try {
//
//            Connection conn = ConnectionPool.getConnection();
//            PreparedStatement stmt = conn.prepareStatement(sql);
//
//            stmt.setString(1, entity.getOperation());
//            stmt.setString(2, entity.getMeasurementType());
//            stmt.setDouble(3, entity.getValue1());
//            stmt.setDouble(4, entity.getValue2());
//            stmt.setBoolean(5, entity.isResult());
//
//            stmt.executeUpdate();
//
//            ConnectionPool.releaseConnection(conn);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
//
