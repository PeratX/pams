package org.itxtech.pams.repo;

import org.itxtech.pams.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepo extends JpaRepository<Asset, Long> {
    List<Asset> findAllByDisabled(boolean disabled);
}
