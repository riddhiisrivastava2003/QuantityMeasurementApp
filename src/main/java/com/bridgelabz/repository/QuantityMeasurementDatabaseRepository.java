//package com.bridgelabz.repository;
//
//import com.bridgelabz.entity.QuantityMeasurementEntity;
//import com.bridgelabz.util.ConnectionPool;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.ArrayList;
//import java.util.List;
//
//public class QuantityMeasurementDatabaseRepository
//        implements IQuantityMeasurementRepository {
//
//    @Override
//    public void save(QuantityMeasurementEntity entity) {
//        String sql = """
//        INSERT INTO quantity_measurement
//        (operation, measurement_type, value1, unit1, value2, unit2, result)
//        VALUES (?, ?, ?, ?, ?, ?, ?)
//        """;
//
//        Connection conn = null;
//        PreparedStatement stmt = null;
//
//        try {
//            conn = ConnectionPool.getConnection(); // fetch connection from pool
//            stmt = conn.prepareStatement(sql);
//
//            stmt.setString(1, entity.getOperation());
//            stmt.setString(2, entity.getMeasurementType());
//            stmt.setDouble(3, entity.getValue1());
//            stmt.setString(4, entity.getUnit1());
//            stmt.setDouble(5, entity.getValue2());
//            stmt.setString(6, entity.getUnit2());
//            stmt.setString(7, entity.getResult());
//
//            stmt.executeUpdate();
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        } finally {
//            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
//            if (conn != null) ConnectionPool.getInstance().releaseConnection(conn); // return connection
//        }
//    }
//
//    @Override
//    public List<QuantityMeasurementEntity> findAll() {
//
//        List<QuantityMeasurementEntity> list = new ArrayList<>();
//
//        String sql = "SELECT * FROM quantity_measurement";
//
//        try (Connection conn = ConnectionPool.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql);
//             ResultSet rs = stmt.executeQuery()) {
//
//            while (rs.next()) {
//
//                QuantityMeasurementEntity entity =
//                        new QuantityMeasurementEntity(
//                                rs.getString("operation"),
//                                rs.getString("measurement_type"),
//                                rs.getDouble("value1"),
//                                rs.getString("unit1"),
//                                rs.getDouble("value2"),
//                                rs.getString("unit2"),
//                                rs.getString("result")
//                        );
//
//                list.add(entity);
//            }
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        return list;
//    }
//
//    @Override
//    public void deleteAll() {
//        String sql = "DELETE FROM quantity_measurement";
//
//        try (Connection conn = ConnectionPool.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.executeUpdate();
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to delete all measurements", e);
//        }
//    }
//
//
//}

package com.bridgelabz.repository;

import com.bridgelabz.entity.QuantityMeasurementEntity;
import com.bridgelabz.util.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class QuantityMeasurementDatabaseRepository implements IQuantityMeasurementRepository {

    @Override
    public void save(QuantityMeasurementEntity entity) {
        String sql = """
                INSERT INTO quantity_measurement
                (operation, measurement_type, value1, unit1, value2, unit2, result)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionPool.getConnection();
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, entity.getOperation());
            stmt.setString(2, entity.getMeasurementType());
            stmt.setDouble(3, entity.getValue1());
            stmt.setString(4, entity.getUnit1());
            stmt.setDouble(5, entity.getValue2());
            stmt.setString(6, entity.getUnit2());
            stmt.setString(7, entity.getResult());

            stmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            if (conn != null) ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    @Override
    public List<QuantityMeasurementEntity> findAll() {
        List<QuantityMeasurementEntity> list = new ArrayList<>();
        String sql = "SELECT * FROM quantity_measurement";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionPool.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                        rs.getString("operation"),
                        rs.getString("measurement_type"),
                        rs.getDouble("value1"),
                        rs.getString("unit1"),
                        rs.getDouble("value2"),
                        rs.getString("unit2"),
                        rs.getString("result")
                );
                list.add(entity);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            if (conn != null) ConnectionPool.getInstance().releaseConnection(conn);
        }

        return list;
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM quantity_measurement";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionPool.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete all measurements", e);
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            if (conn != null) ConnectionPool.getInstance().releaseConnection(conn);
        }
    }
}