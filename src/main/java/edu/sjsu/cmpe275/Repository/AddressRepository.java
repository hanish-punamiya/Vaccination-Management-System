package edu.sjsu.cmpe275.Repository;

import edu.sjsu.cmpe275.Model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}