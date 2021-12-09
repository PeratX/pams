package org.itxtech.pams.repo;

import org.itxtech.pams.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepo extends JpaRepository<Asset, Long> {
}
