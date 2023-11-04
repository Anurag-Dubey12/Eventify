package com.example.eventmatics.RoomDatabase.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.eventmatics.RoomDatabase.DataClas.BudgetEntity
import com.example.eventmatics.RoomDatabase.DataClas.EventEntity
import com.example.eventmatics.RoomDatabase.DataClas.GuestEntity
import com.example.eventmatics.RoomDatabase.DataClas.PaymentEntity
import com.example.eventmatics.RoomDatabase.DataClas.TaskEntity
import com.example.eventmatics.RoomDatabase.DataClas.VendorEntity
import com.example.eventmatics.RoomDatabase.DataClas.VendorPaymentEntity

@Dao
interface EventsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun InserEvent(eventEntity: EventEntity):Long
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun InsertTask(taskEntity: TaskEntity):Long
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun InsertBudget(budgetEntity: BudgetEntity): Long
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun InsertGuest(guestEntity: GuestEntity): Long
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun InsertVendor(vendorEntity: VendorEntity): Long
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun InsertPayment(paymentEntity: PaymentEntity): Long
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun InsertVendorPayment(vendorPaymentEntity: VendorPaymentEntity): Long

    //Event Query
    @Query("SELECT COUNT(*) FROM Event WHERE Event_Name = :eventName")
    fun isEventNameExists(eventName: String): Boolean
    @Query("SELECT * FROM Event LIMIT 5")
    fun getAllEvents(): MutableList<EventEntity>
    @Query("SELECT * FROM Event WHERE id =:eventId")
    fun getEventData(eventId: Long?): EventEntity?

    @Query ("DELETE FROM Event")
    fun deteleAllEvent():Int
    @Delete
    fun deleteEvent(eventEntity: EventEntity)

    //Task Query
    @Query("SELECT * FROM Task ORDER BY task_id ASC")
    fun getAllTasks():MutableList<TaskEntity>
    @Query("UPDATE Task SET Task_Status =:newvalue WHERE task_id = :id")
    fun updateTaskStatus(id:Long,newvalue:String):Int

    @Query("SELECT Task_Status FROM Task WHERE task_id = :taskid ")
    fun isTaskCompleted(taskid:Long):Boolean

    @Query("SELECT * FROM Task WHERE Task_Name LIKE '%' || :query || '%'")
    fun searchTasks(query: String): List<TaskEntity>

    @Query("SELECT COUNT(*) FROM Task")
    fun getTotalTask():Int
    @Query("SELECT * FROM Task WHERE Task_Status = 'Completed' ")
    fun getCompletedTaskCount():Int
    @Query("SELECT * FROM Task WHERE Task_Status = 'Pending' ")
    fun getTaskPendingStatus():Int
    @Update
    fun updateTask(taskEntity: TaskEntity):Int
    @Delete
    fun deleteTask(taskEntity: TaskEntity)

    //Budget Query
    @Query("SELECT * FROM Budget WHERE budget_id = :budgetId")
    fun getBudgetData(budgetId: Long): BudgetEntity?
    @Query("SELECT * FROM Budget WHERE Budget_Name LIKE :query")
    fun searchBudget(query: String): MutableList<BudgetEntity>

    @Query("SELECT Budget_Paid FROM Budget WHERE budget_id = :budgetId")
    fun isBudgetPaid(budgetId: Long): Boolean

    @Query("UPDATE Budget SET Budget_Paid =:newvalue WHERE budget_id = :id")
    fun updateBudgetPaid(id:Long,newvalue:String): Int

    @Query("SELECT SUM(CAST(Budget_Estimated AS REAL)) FROM Budget")
    fun getTotalBudget(): Double

    @Query("SELECT SUM(CAST(Budget_Estimated AS REAL)) FROM Budget WHERE Budget_Paid = 'Paid'")
    fun getTotalPaidBudget(): Double

    @Query("SELECT SUM(CAST(Budget_Estimated AS REAL)) FROM Budget WHERE Budget_Paid = 'Not Paid'")
    fun getTotalNotPaidBudget(): Double

    @Query("SELECT * FROM Budget")
    fun getAllBudgets(): List<BudgetEntity>

    @Update
    fun updateBudget(budget: BudgetEntity): Int

    @Delete
    fun deleteBudget(budget: BudgetEntity): Int

    //Budget Payment Query
    @Query("SELECT * FROM Payment WHERE Budget_payment_id = :budgetId")
    fun getPaymentsForBudget(budgetId: Int): MutableList<PaymentEntity>
    @Query("SELECT SUM(payment_amount) FROM Payment WHERE payment_status = 'Paid' AND Budget_payment_id = :budgetId")
    fun getTotalPaymentAmount(budgetId: Long): Float
    @Query("UPDATE Payment SET Payment_Name = :name, Payment_Amount = :amount, Payment_Date = :date, Payment_Status = :status, Budget_payment_id = :budgetId WHERE Payment_ID = :paymentId")
    fun updatePayment(paymentId: Int, name: String, amount: Double, date: String, status: String, budgetId: Int): Int
    @Delete
    fun DeleteBudgetPayment(payment: PaymentEntity):Int

    //Vendor Payment
    @Query("SELECT * FROM Vendor_Payment WHERE Vendor_payment_id = :vendorId")
    fun getPaymentsForVendor(vendorId: Long): List<VendorPaymentEntity>
    @Query("SELECT SUM(CAST(Vendor_payment_amount AS REAL)) FROM Vendor_Payment WHERE Vendor_payment_status = 'Paid' AND Vendor_payment_id = :vendorId")
    fun getTotalPaymentAmountVendor(vendorId: Long): Float
    @Query("UPDATE Vendor_Payment SET Vendor_payment_name = :newName, Vendor_payment_amount = :newAmount, Vendor_payment_date = :newDate, Vendor_payment_status = :newStatus WHERE payment_id = :paymentId")
    fun updateVendorPayment(paymentId: Int, newName: String, newAmount: Float, newDate: String, newStatus: String)
    @Delete
    fun DeleteVendorPayment(payment: VendorPaymentEntity):Int

    //Guest Query
    @Query("SELECT * FROM Guest")
    fun getAllGuests(): MutableList<GuestEntity>

    @Query("SELECT * FROM Guest WHERE Guest_Name LIKE '%' || :query || '%'")
    fun searchGuests(query: String): MutableList<GuestEntity>

    @Query("SELECT COUNT(*) FROM Guest WHERE guest_status = 'Invitation Sent'")
    fun getTotalInvitationsSent(): Int

    @Query("SELECT COUNT(*) FROM Guest")
    fun getTotalInvitations(): Int

    @Query("SELECT COUNT(*) FROM Guest WHERE guest_status = 'Not Sent'")
    fun getTotalInvitationsNotSent(): Int

    @Query("SELECT guest_status FROM Guest WHERE id = :guestId")
    fun isInvitationSent(guestId: Long): Boolean

    @Update
    fun updateGuest(guest: GuestEntity): Int

    @Query("UPDATE Guest SET guest_status = :newStatus WHERE id = :guestId")
    fun updateGuestInvitation(guestId: Long, newStatus: String): Int

    @Delete
    fun deleteGuest(guest: GuestEntity)

    //Vendor Query
    @Query("SELECT * FROM Vendor")
    fun getAllVendors(): MutableList<VendorEntity>
    @Query("SELECT * FROM Vendor WHERE Vendor_Name LIKE '%' || :query || '%'")
    fun searchVendors(query: String): MutableList<VendorEntity>

    @Query("SELECT SUM(CAST(Vendor_Estimated AS REAL)) FROM Vendor")
    fun getTotalVendorBudget(): Double

    @Query("SELECT SUM(CAST(Vendor_Estimated AS REAL)) FROM Vendor WHERE Vendor_Paid = 'Paid'")
    fun getVendorPaidAmount(): Double

    @Query("SELECT SUM(CAST(Vendor_Estimated AS REAL)) FROM Vendor WHERE Vendor_Paid = 'Not Paid'")
    fun getVendorNotPaidAmount(): Double

    @Query("SELECT Vendor_Paid FROM Vendor WHERE id = :vendorId")
    fun isVendorPaid(vendorId: Long): Boolean

    @Query("UPDATE Vendor SET Vendor_Paid = :newvalue WHERE id = :id")
    fun updateVendorPaid(id: Long, newvalue: String)


    @Update
    fun updateVendor(vendor: VendorEntity)

    @Delete
    fun deleteVendor(vendor: VendorEntity)
}