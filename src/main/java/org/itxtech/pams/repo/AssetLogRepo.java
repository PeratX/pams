package org.itxtech.pams.repo;

import org.itxtech.pams.model.AssetLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetLogRepo extends JpaRepository<AssetLog, Long> {
    List<AssetLog> findByAsset_Id(Long id);
}
