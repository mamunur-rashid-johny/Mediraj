package com.example.mediraj.localdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mediraj.model.checkout.Checkout;

import java.util.List;

@Dao
public interface SurgicalServiceDao {
    @Query("SELECT * FROM surgical_services")
    List<SurgicalService> getAllSurgical();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertInfo(SurgicalService surgicalService);
    @Query("DELETE FROM surgical_services")
    abstract void deleteAllData_surgical();

    @Query("DELETE FROM surgical_services WHERE id = :id")
    abstract void deleteByIdOne(long id);

    @Query("DELETE FROM surgical_services WHERE id = :id")
    abstract void deleteById_Surgical(int id);

    @Query("UPDATE surgical_services SET item_qty =:quantity ,item_subtotal=:total WHERE id =:id")
    void updateUser_surgical(int id,int quantity,int total);

    @Query("SELECT item_id,item_unit,item_qty,item_price,item_subtotal FROM surgical_services")
    List<Checkout> getCheckoutData_surgical();

    @Query("delete from surgical_services where id in (:idList)")
    void deleteByIdList(List<Long> idList);
}
