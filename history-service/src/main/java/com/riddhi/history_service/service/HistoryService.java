package com.riddhi.history_service.service;

import com.riddhi.history_service.entity.OperationHistory;
import com.riddhi.history_service.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class HistoryService {

    @Autowired
    private HistoryRepository repository;

    public OperationHistory save(Map<String, Object> data) {
        OperationHistory h = new OperationHistory();
        h.setUsername((String) data.get("username"));
        h.setOperation((String) data.getOrDefault("operation", "CONVERT"));
        h.setFromUnit((String) data.get("fromUnit"));
        h.setToUnit((String) data.get("toUnit"));
        h.setMeasurementType((String) data.getOrDefault("measurementType", "UNKNOWN"));
        Object val = data.get("inputValue");
        if (val instanceof Number) h.setInputValue(((Number) val).doubleValue());
        h.setResult(String.valueOf(data.get("result")));
        return repository.save(h);
    }

    public List<OperationHistory> getByUsername(String username) {
        return repository.findByUsernameOrderByCreatedAtDesc(username);
    }

    @Transactional
    public void clearByUsername(String username) {
        repository.deleteByUsername(username);
    }

    public List<OperationHistory> getAll() {
        return repository.findAll();
    }
}
