package com.finsol.main.aspects.repository;

import com.finsol.main.aspects.model.ExceptionCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Exception_Repo extends JpaRepository<ExceptionCount, Long>
{

}
