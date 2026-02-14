package com.example.price_monitoring_system.service;

import com.example.price_monitoring_system.domain.ItemSnapshot;
import com.example.price_monitoring_system.dto.ItemSnapshotRequestDto;

import java.util.List;

public interface ItemSnapshotService {
    List<ItemSnapshot> saveItemSnapshots(List<ItemSnapshotRequestDto> itemSnapshotsRequests);
}
