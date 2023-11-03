package com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.Budget
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.DatabaseNameDataClass
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.Events
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.Guest
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.Task
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.Vendor
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.Paymentinfo
import com.example.eventmatics.SQLiteDatabase.Dataclass.data_class.VendorPaymentinfo

class LocalDatabase(contex:Context,databasename:String):
    SQLiteOpenHelper(contex,databasename,null,DATABASE_VERSION) {
    companion object{
        private const val DATABASE_VERSION = 1
        // Table names
        private const val TABLE_Event = "Event"
        private const val TABLE_TASK = "Task"
        private const val TABLE_VENDOR = "Vendor"
        private const val TABLE_GUEST = "Guest"
        private const val TABLE_BUDGET = "Budget"
        private const val TABLE_Payment = "Payment"
        private const val Vendor_TABLE_Payment = "Vendor_Payment"

        // Common column names
        private const val COLUMN_ID = "id"

        //Event Column Name
        private const val Event_Name = "Event_Name"
        private const val Event_Date = "Event_Date"
        private const val Event_Time = "Event_Time"
        private const val Event_Budget = "Event_Budget"
        private const val UserID = "UserID"

        //Task Column Name
        private const val Task_Name = "Task_Name"
        private const val Task_Category = "Task_Category"
        private const val Task_Note = "Task_Note"
        private const val Task_Status = "Task_Status"
        private const val Task_Date = "Task_Date"


        //Budget Column Name
        private const val Budget_Name = "Budget_Name"
        private const val Budget_Category = "Budget_Category"
        private const val Budget_Note = "Budget_Note"
        private const val Budget_Estimated = "Budget_Estimated"
        private const val Budget_Balance = "Budget_Balance"
        private const val Budget_Pending = "Budget_Pending"
        private const val Budget_Paid = "Budget_Paid"

        //Budget Payment Column Name
        private const val Payment_ID = "Payment_ID"
        private const val Payment_Name = "Payment_Name"
        private const val Payment_Amount = "Payment_Amount"
        private const val Payment_Status = "Payment_Status"
        private const val Payment_Date = "Payment_Date"
        private const val Payment_BudgetID = "Payment_BudgetID"

        //Vendor Payment Column Name
        private const val Vendor_Payment_ID = "Vendor_Payment_ID"
        private const val Vendor_Payment_Name = "Vendor_Payment_Name"
        private const val Vendor_Payment_Amount = "Vendor_Payment_Amount"
        private const val Vendor_Payment_Status = "Vendor_Payment_Status"
        private const val Vendor_Payment_Date = "Vendor_Payment_Date"
        private const val Payment_VendorID = "Payment_VendorID"

        //Guest Column Name
        private const val Guest_Name = "Guest_Name"
        private const val TOTAL_FAMILY_MEMBERS = "total_family_members"
        private const val NOTE = "note"
        private const val GUEST_STATUS = "guest_status"
        private const val GUEST_CONTACT = "guest_contact"
//        private const val GUEST_EMAIL = "guest_email"
        private const val GUEST_Acceptence_Status = "Guest_Acceptence_Status"
        private const val GUEST_ADDRESS = "guest_address"

        //Vendor Column Name
        private const val Vendor_Name = "Vendor_Name"
        private const val Vendor_Category = "Vendor_Category"
        private const val Vendor_Note = "Vendor_Note"
        private const val Vendor_Estimated = "Vendor_Estimated"
        private const val Vendor_Balance = "Vendor_Balance"
        private const val Vendor_Pending = "Vendor_Pending"
        private const val Vendor_Paid = "Vendor_Paid"
        private const val Vendor_PhoneNumber = "Vendor_PhoneNumber"
        private const val Vendor_EmailId = "Vendor_EmailId"
        private const val Vendor_Website = "Vendor_Website"
        private const val Vendor_Address = "Vendor_Address"

    }
    override fun onCreate(db: SQLiteDatabase?) {
        val createEventTableQuery = "CREATE TABLE $TABLE_Event (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$Event_Name TEXT," +
                "$Event_Date TEXT," +
                "$Event_Time TEXT," +
                "$Event_Budget TEXT," +
                "$UserID TEXT "+
                ")"
        db?.execSQL(createEventTableQuery)

        val createTaskTableQuery = "CREATE TABLE $TABLE_TASK (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$Task_Name TEXT," +
                "$Task_Date TEXT," +
                "$Task_Status TEXT," +
                "$Task_Category TEXT," +
                "$Task_Note TEXT" +
                ")"
        db?.execSQL(createTaskTableQuery)

        val createVendorTableQuery = "CREATE TABLE $TABLE_VENDOR (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$Vendor_Name TEXT," +
                "$Vendor_Category TEXT," +
                "$Vendor_Note TEXT," +
                "$Vendor_Estimated TEXT," +
                "$Vendor_Balance TEXT," +
                "$Vendor_Pending TEXT," +
                "$Vendor_Paid TEXT," +
                "$Vendor_PhoneNumber TEXT," +
                "$Vendor_EmailId TEXT," +
                "$Vendor_Website TEXT," +
                "$Vendor_Address TEXT" +
                ")"
        db?.execSQL(createVendorTableQuery)

        val createGuestTableQuery = "CREATE TABLE $TABLE_GUEST (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$Guest_Name TEXT," +
                "$TOTAL_FAMILY_MEMBERS INTEGER," +
                "$NOTE TEXT," +
                "$GUEST_STATUS TEXT," +
                "$GUEST_CONTACT TEXT," +
                "$GUEST_Acceptence_Status TEXT," +
                "$GUEST_ADDRESS TEXT" +
                ")"
        db?.execSQL(createGuestTableQuery)

        val createBudgetTableQuery = "CREATE TABLE $TABLE_BUDGET (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$Budget_Name TEXT," +
                "$Budget_Category TEXT," +
                "$Budget_Note TEXT," +
                "$Budget_Estimated TEXT," +
                "$Budget_Balance TEXT," +
                "$Budget_Pending TEXT," +
                "$Budget_Paid TEXT" +
                ")"
        db?.execSQL(createBudgetTableQuery)

        val CREATE_PAYMENT_TABLE = "CREATE TABLE $TABLE_Payment ("+
                "$Payment_ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "$Payment_Name TEXT,"+
                "$Payment_Amount REAL,"+
                "$Payment_Status TEXT,"+
                "$Payment_Date TEXT,"+
                "$Payment_BudgetID INTEGER,"+
                "FOREIGN KEY ($Payment_BudgetID) REFERENCES $TABLE_BUDGET($COLUMN_ID)"+
                ")"

        db?.execSQL(CREATE_PAYMENT_TABLE)
        val CREATE_VENDOR_PAYMENT_TABLE = "CREATE TABLE $Vendor_TABLE_Payment ("+
                "$Vendor_Payment_ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "$Vendor_Payment_Name TEXT,"+
                "$Vendor_Payment_Amount REAL,"+
                "$Vendor_Payment_Status TEXT,"+
                "$Vendor_Payment_Date TEXT,"+
                "$Payment_VendorID INTEGER,"+
                "FOREIGN KEY ($Payment_VendorID) REFERENCES $TABLE_VENDOR($COLUMN_ID)"+
                ")"

        db?.execSQL(CREATE_VENDOR_PAYMENT_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if(oldVersion<2){
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_Event")
            val createEventTableQuery = "CREATE TABLE $TABLE_Event (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$Event_Name TEXT," +
                    "$Event_Date TEXT," +
                    "$Event_Time TEXT," +
                    "$Event_Budget TEXT," +
                    "$UserID TEXT "+
                    ")"
            db?.execSQL(createEventTableQuery)
        }

        if(oldVersion<3){
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_TASK")
            val createTaskTableQuery = "CREATE TABLE $TABLE_TASK (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$Task_Name TEXT," +
                    "$Task_Category TEXT," +
                    "$Task_Note TEXT," +
                    "$Task_Status TEXT," +
                    "$Task_Date TEXT" +
                    ")"
            db?.execSQL(createTaskTableQuery)
        }
        if(oldVersion<4){
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_BUDGET")
            val createBudgetTableQuery = "CREATE TABLE $TABLE_BUDGET (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$Budget_Name TEXT," +
                    "$Budget_Category TEXT," +
                    "$Budget_Note TEXT," +
                    "$Budget_Estimated TEXT," +
                    "$Budget_Balance TEXT," +
                    "$Budget_Pending TEXT," +
                    "$Budget_Paid TEXT" +
                    ")"
            db?.execSQL(createBudgetTableQuery)
        }
        if(oldVersion<5){
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_GUEST")
            val createGuestTableQuery = "CREATE TABLE $TABLE_GUEST (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$Guest_Name TEXT," +
                    "$TOTAL_FAMILY_MEMBERS TEXT," +
                    "$NOTE TEXT," +
                    "$GUEST_STATUS TEXT," +
                    "$GUEST_CONTACT TEXT," +
                    "$GUEST_Acceptence_Status TEXT," +
                    "$GUEST_ADDRESS TEXT" +
                    ")"
            db?.execSQL(createGuestTableQuery)
        }
        if(oldVersion<6){
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_VENDOR")
            val createVendorTableQuery = "CREATE TABLE $TABLE_VENDOR (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$Vendor_Name TEXT," +
                    "$Vendor_Category TEXT," +
                    "$Vendor_Note TEXT," +
                    "$Vendor_Estimated TEXT," +
                    "$Vendor_Balance TEXT," +
                    "$Vendor_Pending TEXT," +
                    "$Vendor_Paid TEXT," +
                    "$Vendor_PhoneNumber TEXT," +
                    "$Vendor_EmailId TEXT," +
                    "$Vendor_Website TEXT," +
                    "$Vendor_Address TEXT" +
                    ")"
            db?.execSQL(createVendorTableQuery)
        }
        if(oldVersion<7){
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_Payment")
            val CREATE_PAYMENT_TABLE = "CREATE TABLE $TABLE_Payment ("+
                    "$Payment_ID INTEGER PRIMARY KEY,"+
                    "$Payment_Name TEXT,"+
                    "$Payment_Amount REAL,"+
                    "$Payment_Status TEXT,"+
                    "$Payment_Date TEXT,"+
                    "$Payment_BudgetID INTEGER,"+
                    "FOREIGN KEY ($Payment_BudgetID) REFERENCES $TABLE_BUDGET($COLUMN_ID)"+
                    ")"
            db?.execSQL(CREATE_PAYMENT_TABLE)
        }
        onCreate(db)
        if(oldVersion<8){
            db?.execSQL("DROP TABLE IF EXISTS $Vendor_TABLE_Payment")
            val CREATE_PAYMENT_TABLE = "CREATE TABLE $Vendor_TABLE_Payment ("+
                    "$Vendor_Payment_ID INTEGER PRIMARY KEY,"+
                    "$Vendor_Payment_Name TEXT,"+
                    "$Vendor_Payment_Amount REAL,"+
                    "$Vendor_Payment_Status TEXT,"+
                    "$Vendor_Payment_Date TEXT,"+
                    "$Payment_VendorID INTEGER,"+
                    "FOREIGN KEY ($Payment_VendorID) REFERENCES $TABLE_VENDOR($COLUMN_ID)"+
                    ")"
            db?.execSQL(CREATE_PAYMENT_TABLE)
        }
        onCreate(db)
    }
    fun createEvent(event: Events): Long {
        if (isEventNameExists(event.name)) {
            return -1
        }
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Event_Name, event.name)
            put(Event_Date, event.Date)
            put(Event_Time, event.time)
            put(Event_Budget, event.budget)
            put(UserID, event.userid)
        }
        val id = db.insert(TABLE_Event, null, values)
        db.close()
        return id
    }
    fun isEventNameExists(eventName: String): Boolean {
        val db = readableDatabase
        val query = "SELECT $Event_Name FROM $TABLE_Event WHERE $Event_Name = ?"
        val cursor = db.rawQuery(query, arrayOf(eventName))
        val eventExists = cursor.count > 0
        cursor.close()
        db.close()
        return eventExists
    }
    @SuppressLint("Range")
    fun getAllEvents(): MutableList<Events> {
        val events = ArrayList<Events>()
        val selectQuery = "SELECT * FROM $TABLE_Event LIMIT 5"
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery(selectQuery, null)
        cursor?.let {
            if (it.moveToFirst()) {
                do {
                    val id = it.getLong(it.getColumnIndex(COLUMN_ID))
                    val name = it.getString(it.getColumnIndex(Event_Name))
                    val date = it.getString(it.getColumnIndex(Event_Date))
                    val time = it.getString(it.getColumnIndex(Event_Time))
                    val budget = it.getString(it.getColumnIndex(Event_Budget))
                    val userid = cursor.getString(cursor.getColumnIndex(UserID))
                    val event = Events(id,name, date, time, budget,userid)
                    events.add(event)
                } while (it.moveToNext())
            }
        }
        cursor?.close()
        db.close()
        return events
    }

    //Getting Specific data
    @SuppressLint("Range")
    fun getEventData(eventId: Long?): Events? {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_Event WHERE $COLUMN_ID = $eventId"
        val cursor = db.rawQuery(query, null)
        var event: Events? = null

        if (cursor.moveToFirst()) {
            val id=cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
            val eventName = cursor.getString(cursor.getColumnIndex(Event_Name))
            val eventDate = cursor.getString(cursor.getColumnIndex(Event_Date))
            val eventTime = cursor.getString(cursor.getColumnIndex(Event_Time))
            val eventBudget = cursor.getString(cursor.getColumnIndex(Event_Budget))
            val userid = cursor.getString(cursor.getColumnIndex(UserID))
            event = Events( id,eventName, eventDate, eventTime, eventBudget,userid)
        }

        cursor.close()
        return event
    }

//     Delete an event
    fun deleteEvent(event: Events): Int {
        val db = writableDatabase
        val rowsAffected = db.delete(
            TABLE_Event,
            "$COLUMN_ID = ?",
            arrayOf(event.id.toString())
        )
        db.close()
        return rowsAffected
    }
    fun deteleAllEvent():Int{
        val db=writableDatabase
        val rowsaffected=db.delete(TABLE_Event,null,null)
        db.close()
        return rowsaffected
    }

    //Task Function
    fun createTask(task: Task): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Task_Name, task.taskName)
            put(Task_Date, task.taskDate)
            put(Task_Note, task.taskNote)
            put(Task_Category, task.category)
            put(Task_Status, task.taskStatus)
        }
        val id = db.insert(TABLE_TASK, null, values)
        db.close()
        return id
    }

    // Get all tasks
    @SuppressLint("Range")
    fun getAllTasks(): MutableList<Task> {
        val tasks = ArrayList<Task>()
        val selectQuery = "SELECT * FROM $TABLE_TASK ORDER BY $COLUMN_ID ASC"
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery(selectQuery, null)
        cursor?.let {
            if (it.moveToFirst()) {
                do {
                    val id = it.getLong(it.getColumnIndex(COLUMN_ID))
                    val name = it.getString(it.getColumnIndex(Task_Name))
                    val category = it.getString(it.getColumnIndex(Task_Category))
                    val note = it.getString(it.getColumnIndex(Task_Note))
                    val status = it.getString(it.getColumnIndex(Task_Status))
                    val date = it.getString(it.getColumnIndex(Task_Date))
                    val task = Task( id,name,category, note, status,date)
                    tasks.add(task)
                } while (it.moveToNext())
            }
        }
        cursor?.close()
        db.close()
        return tasks
    }

    fun UpdateTaskStatus(id:Long,newvalue:String):Int{
        val db=writableDatabase
        val value=ContentValues().apply {
            put(Task_Status,newvalue)
        }
        val rowaffected=db.update(TABLE_TASK,value,"$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return rowaffected
    }


    @SuppressLint("Range")
    fun isTaskCompleted(Taskid:Long):Boolean{
        val db=readableDatabase
        val query="SELECT $Task_Status FROM $TABLE_TASK WHERE $COLUMN_ID = ?"
        val cursor=db.rawQuery(query, arrayOf(Taskid.toString()))
        var iscompleted=false
        if(cursor.moveToFirst()){
            val completedvalue=cursor.getString(cursor.getColumnIndex(Task_Status))
            if(completedvalue=="Completed"){
                iscompleted=true
            }
        }
        cursor.close()
        db.close()
        return iscompleted
    }

    //Saerch For Task
    @SuppressLint("Range")
    fun searchTask(query: String): MutableList<Task> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_TASK,
            null,
            "$Task_Name LIKE ?",
            arrayOf("%$query%"),
            null,
            null,
            null
        )
        val taskList = mutableListOf<Task>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
                val taskName = cursor.getString(cursor.getColumnIndex(Task_Name))
                val taskCategory = cursor.getString(cursor.getColumnIndex(Task_Category))
                val taskNote = cursor.getString(cursor.getColumnIndex(Task_Note))
                val taskStatus = cursor.getString(cursor.getColumnIndex(Task_Status))
                val taskDate = cursor.getString(cursor.getColumnIndex(Task_Date))

                taskList.add(Task(id, taskName, taskCategory, taskNote, taskStatus, taskDate))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return taskList
    }

    fun getTotalTask():Int{
        var total=0
        val db=readableDatabase
        val query="SELECT COUNT(*) FROM $TABLE_TASK"
        val Cursor=db.rawQuery(query,null)
        Cursor?.let {
            if(it.moveToNext()){
                total=it.getInt(0)
            }
        }
        Cursor?.close()
        db.close()
        return total
    }
    fun getTaskStatus():Int{
        var total=0
        val db=readableDatabase
        val query="SELECT COUNT(*) FROM $TABLE_TASK WHERE $Task_Status = 'Completed' "
        val cursor:Cursor?=db.rawQuery(query,null)
        cursor?.let {
            if(it.moveToNext()){
                total=it.getInt(0)
            }
        }
        cursor?.close()
        db.close()
        return total
    }
    fun getTaskPendingStatus():Int{
        var total=0
        val db=readableDatabase
        val query="SELECT COUNT(*) FROM $TABLE_TASK WHERE $Task_Status = 'Pending'"
        val cursor=db.rawQuery(query, null)
        cursor?.let {
            if (it.moveToFirst()){
                total=it.getInt(0)
            }
        }
        cursor?.close()
        db.close()
        return total
    }

    //     Update a task
    fun updateTask(task: Task): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Task_Name, task.taskName)
            put(Task_Category, task.category)
            put(Task_Note, task.taskNote)
            put(Task_Status, task.taskStatus)
            put(Task_Date, task.taskDate)
        }
        val rowsAffected = db.update(
            TABLE_TASK,
            values,
            "$COLUMN_ID = ?",
            arrayOf(task.id.toString())
        )
        db.close()
        return rowsAffected
    }


    // Delete a task
    fun deleteTask(task: Task): Int {
        val db = writableDatabase
        val rowsAffected = db.delete(
            TABLE_TASK,
            "$COLUMN_ID = ?",
            arrayOf(task.id.toString())
        )
        db.close()
        return rowsAffected
    }

    // Create new budget
    fun createBudget(budget: Budget): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Budget_Name, budget.name)
            put(Budget_Category, budget.category)
            put(Budget_Note, budget.note)
            put(Budget_Estimated, budget.estimatedAmount)
            put(Budget_Balance, budget.balance)
            put(Budget_Pending, budget.remaining)
            put(Budget_Paid, budget.paid)
        }
        val id = db.insert(TABLE_BUDGET, null, values)
        db.close()
        return id
    }


    //Getting Specific data for budget
    @SuppressLint("Range")
    fun getBudgetData(budgetId: Int): Budget? {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_BUDGET WHERE $COLUMN_ID = $budgetId"
        val cursor = db.rawQuery(query, null)
        var budget: Budget? = null

        if (cursor.moveToFirst()) {
            val budgetName = cursor.getString(cursor.getColumnIndex(Budget_Name))
            val budgetCategory = cursor.getString(cursor.getColumnIndex(Budget_Category))
            val budgetNote = cursor.getString(cursor.getColumnIndex(Budget_Note))
            val budgetEstimated = cursor.getString(cursor.getColumnIndex(Budget_Estimated))
            val budgetBalance = cursor.getString(cursor.getColumnIndex(Budget_Balance))
            val budgetPending = cursor.getString(cursor.getColumnIndex(Budget_Pending))
            val budgetPaid = cursor.getString(cursor.getColumnIndex(Budget_Paid))

            budget = Budget(budgetId.toLong(), budgetName, budgetCategory, budgetNote, budgetEstimated, budgetBalance, budgetPending, budgetPaid)
        }

        cursor.close()
        return budget
    }

    @SuppressLint("Range")
    fun SearchBudget(query: String):MutableList<Budget>{
        val db=readableDatabase
        val cursor=db.query(
            TABLE_BUDGET,
            null,
            "$Budget_Name LIKE ?",
            arrayOf("%$query%"),
            null,
            null,
            null
        )
        var BudgetList= mutableListOf<Budget>()
    if(cursor.moveToFirst()){
        do{
            val budgetId=cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
            val budgetName = cursor.getString(cursor.getColumnIndex(Budget_Name))
            val budgetCategory = cursor.getString(cursor.getColumnIndex(Budget_Category))
            val budgetNote = cursor.getString(cursor.getColumnIndex(Budget_Note))
            val budgetEstimated = cursor.getString(cursor.getColumnIndex(Budget_Estimated))
            val budgetBalance = cursor.getString(cursor.getColumnIndex(Budget_Balance))
            val budgetPending = cursor.getString(cursor.getColumnIndex(Budget_Pending))
            val budgetPaid = cursor.getString(cursor.getColumnIndex(Budget_Paid))

            BudgetList.add(Budget(budgetId.toLong(), budgetName, budgetCategory, budgetNote, budgetEstimated, budgetBalance, budgetPending, budgetPaid))

        }while (cursor.moveToNext())

    }
        cursor.close()
        return BudgetList
    }
    @SuppressLint("Range")
    fun isBudgetPaid(budgetId: Long): Boolean {
        val db = readableDatabase
        val query = "SELECT $Budget_Paid FROM $TABLE_BUDGET WHERE $COLUMN_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(budgetId.toString()))
        var isPaid = false

        if (cursor.moveToFirst()) {
            val paidValue = cursor.getString(cursor.getColumnIndex(Budget_Paid))
            if (paidValue == "Paid") {
                isPaid = true
            }
        }

        cursor.close()
        db.close()
        return isPaid
    }
    //Function to update Budget Value
    fun updateBudgetPaid(id:Long,newvalue:String):Int{
        val db=writableDatabase
        val value=ContentValues().apply {
            put(Budget_Paid,newvalue)
//            put(Budget_Balance,newBalanceValue)
        }
        val rowaffected=db.update(TABLE_BUDGET,value,"$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return rowaffected
    }
    fun getTotalBudget(): Double {
        var totalBudget = 0.0
        val selectQuery = "SELECT SUM(CAST(${Budget_Estimated} AS REAL)) FROM $TABLE_BUDGET"
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery(selectQuery, null)
        cursor?.let {
            if (it.moveToFirst()) {
                totalBudget = it.getDouble(0)
            }
        }
        cursor?.close()
        db.close()
        return totalBudget
    }
    fun getTotalPaidBudget(): Double {
        var totalBudget = 0.0
        val selectQuery = "SELECT SUM(CAST(${Budget_Estimated} AS REAL)) FROM $TABLE_BUDGET WHERE $Budget_Paid= 'Paid'"
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery(selectQuery, null)
        cursor?.let {
            if (it.moveToFirst()) {
                totalBudget = it.getDouble(0)
            }
        }
        cursor?.close()
        db.close()
        return totalBudget
    }
    fun getTotalNotPaidBudget(): Double {
        var totalBudget = 0.0
        val selectQuery = "SELECT SUM(CAST(${Budget_Estimated} AS REAL)) FROM $TABLE_BUDGET WHERE $Budget_Paid= 'Not Paid'"
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery(selectQuery, null)
        cursor?.let {
            if (it.moveToFirst()) {
                totalBudget = it.getDouble(0)
            }
        }
        cursor?.close()
        db.close()
        return totalBudget
    }
    // Get all budgets
    @SuppressLint("Range")
    fun getAllBudgets(): MutableList<Budget> {
        val budgets = ArrayList<Budget>()
        val selectQuery = "SELECT * FROM $TABLE_BUDGET"
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery(selectQuery, null)
        cursor?.let {
            if (it.moveToFirst()) {
                do {
                    val id = it.getLong(it.getColumnIndex(COLUMN_ID))
                    val name = it.getString(it.getColumnIndex(Budget_Name))
                    val category = it.getString(it.getColumnIndex(Budget_Category))
                    val note = it.getString(it.getColumnIndex(Budget_Note))
                    val estimated = it.getString(it.getColumnIndex(Budget_Estimated))
                    val balance = it.getString(it.getColumnIndex(Budget_Balance))
                    val pending = it.getString(it.getColumnIndex(Budget_Pending))
                    val paid = it.getString(it.getColumnIndex(Budget_Paid))
                    val budget = Budget(id, name, category, note, estimated, balance, pending, paid)
                    budgets.add(budget)
                } while (it.moveToNext())
            }
        }
        cursor?.close()
        db.close()
        return budgets
    }
    // Update a budget
    fun updateBudget(budget: Budget): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Budget_Name, budget.name)
            put(Budget_Category, budget.category)
            put(Budget_Note, budget.note)
            put(Budget_Estimated, budget.estimatedAmount)
            put(Budget_Balance, budget.balance)
            put(Budget_Pending, budget.remaining)
            put(Budget_Paid, budget.paid)
        }
        val rowsAffected = db.update(
            TABLE_BUDGET,
            values,
            "$COLUMN_ID = ?",
            arrayOf(budget.id.toString())
        )
        db.close()
        return rowsAffected
    }
    // Delete a budget
    fun deleteBudget(budget: Budget): Int {
        val db = writableDatabase
        val rowsAffected = db.delete(
            TABLE_BUDGET,
            "$COLUMN_ID = ?",
            arrayOf(budget.id.toString())
        )
        db.close()
        return rowsAffected
    }
    fun createPayment(payment: Paymentinfo): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Payment_Name, payment.name)
            put(Payment_Amount, payment.amount)
            put(Payment_Date, payment.date)
            put(Payment_Status, payment.status)
            put(Payment_BudgetID, payment.budgetid)
        }

        return try {
            val newRowId = db.insert(TABLE_Payment, null, values)
            db.close()
            newRowId
        } catch (e: Exception) {
            Log.e("createPayment", "Error creating payment: ${e.message}")
            -1
        }
    }
    fun createVendorPayment(payment: VendorPaymentinfo) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Vendor_Payment_Name, payment.name)
            put(Vendor_Payment_Amount, payment.amount)
            put(Vendor_Payment_Date, payment.date)
            put(Vendor_Payment_Status, payment.status)
            put(Payment_VendorID, payment.VendorId)
        }
        db.insert(Vendor_TABLE_Payment, null, values)
        db.close()
    }
    @SuppressLint("Range")
    fun getPaymentsForBudget(budgetId: Int): MutableList<Paymentinfo> {
        val paymentList = mutableListOf<Paymentinfo>()

        val query = "SELECT $TABLE_Payment.$Payment_ID, $TABLE_Payment.$Payment_Name,$TABLE_Payment.$Payment_Amount, $TABLE_Payment.$Payment_Date," +
                "$TABLE_Payment.$Payment_Status, $TABLE_Payment.$Payment_BudgetID FROM $TABLE_Payment" +
                " INNER JOIN $TABLE_BUDGET ON $TABLE_Payment.$Payment_BudgetID = $TABLE_BUDGET.$COLUMN_ID " +
                "WHERE $TABLE_BUDGET.$COLUMN_ID = $budgetId"
        val db = readableDatabase
        val cursor = db.rawQuery(query, null)
        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndex(Payment_ID))
                val name = it.getString(it.getColumnIndex(Payment_Name))
                val amount = it.getFloat(it.getColumnIndex(Payment_Amount))
                val date = it.getString(it.getColumnIndex(Payment_Date))
                val status = it.getString(it.getColumnIndex(Payment_Status))
                val budgetid = it.getLong(it.getColumnIndex(Payment_BudgetID))
                paymentList.add(Paymentinfo(id, name, amount, date, status, budgetid))
                Log.d("PaymentData","The payment Data is :$paymentList")
            }
        }

        return paymentList
    }
    @SuppressLint("Range")
    fun getPaymentForVendor(VendorId: Int): MutableList<VendorPaymentinfo> {
        val paymentlist = mutableListOf<VendorPaymentinfo>()
        val query = "SELECT $Vendor_TABLE_Payment.$Vendor_Payment_ID,$Vendor_TABLE_Payment.$Vendor_Payment_Name,$Vendor_TABLE_Payment.$Vendor_Payment_Amount,$Vendor_TABLE_Payment.$Vendor_Payment_Date," +
                "$Vendor_TABLE_Payment.$Vendor_Payment_Status,$Vendor_TABLE_Payment.$Payment_VendorID FROM $Vendor_TABLE_Payment" +
                " INNER JOIN $TABLE_VENDOR ON $Vendor_TABLE_Payment.$Payment_VendorID=$TABLE_VENDOR.$COLUMN_ID " +
                "WHERE $TABLE_VENDOR.$COLUMN_ID = $VendorId"
        val db = readableDatabase
        val cursor = db.rawQuery(query, null)
        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndex(Vendor_Payment_ID))
                val name = it.getString(it.getColumnIndex(Vendor_Payment_Name))
                val amount = it.getFloat(it.getColumnIndex(Vendor_Payment_Amount))
                val date = it.getString(it.getColumnIndex(Vendor_Payment_Date))
                val status = it.getString(it.getColumnIndex(Vendor_Payment_Status))
                val VendorId = it.getLong(it.getColumnIndex(Payment_VendorID))
                paymentlist.add(VendorPaymentinfo(id, name, amount, date, status, VendorId))
            }
        }
        return paymentlist
    }
    fun getTotalPaymentAmount(budgetId: Int): Int {
        var total = 0
        val db = readableDatabase
        val query = "SELECT SUM(CAST(${Payment_Amount} AS REAL)) FROM $TABLE_Payment WHERE $Payment_Status='Paid' AND $Payment_BudgetID = ?"
        val cursor = db.rawQuery(query, arrayOf(budgetId.toString()))
        cursor?.let {
            if (it.moveToFirst()) {
                total = it.getInt(0)
            }
        }
        cursor.close()
        db.close()
        return total
    }
    fun getTotalPaymentAmountVendor(VendorID: Int): Int {
        var total = 0
        val db = readableDatabase
        val query = "SELECT SUM(CAST(${Vendor_Payment_Amount} AS REAL)) FROM $Vendor_TABLE_Payment WHERE $Vendor_Payment_Status='Paid' AND $Payment_VendorID = ?"
        val cursor = db.rawQuery(query, arrayOf(VendorID.toString()))
        cursor?.let {
            if (it.moveToFirst()) {
                total = it.getInt(0)
            }
        }
        cursor.close()
        db.close()
        return total
    }
    fun updatePayment(paymentId: Int, newPayment: Paymentinfo): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Payment_Name, newPayment.name)
            put(Payment_Amount, newPayment.amount)
            put(Payment_Date, newPayment.date)
            put(Payment_Status, newPayment.status)
            put(Payment_BudgetID, newPayment.budgetid)
        }
        val rowsAffected = db.updateWithOnConflict(TABLE_Payment, values, "$Payment_ID = ?", arrayOf(paymentId.toString()),SQLiteDatabase.CONFLICT_REPLACE)
        db.close()
        return rowsAffected > 0
    }
    fun updateVendorPayment(paymentId: Int, newPayment: VendorPaymentinfo): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Vendor_Payment_Name, newPayment.name)
            put(Vendor_Payment_Amount, newPayment.amount)
            put(Vendor_Payment_Date, newPayment.date)
            put(Vendor_Payment_Status, newPayment.status)
            put(Payment_VendorID, newPayment.VendorId)
        }

        val rowAffected = db.updateWithOnConflict(Vendor_TABLE_Payment, values, "$Vendor_Payment_ID = ?", arrayOf(paymentId.toString()),SQLiteDatabase.CONFLICT_REPLACE)
        db.close()

        return rowAffected > 0
    }


    // Create new guest
    fun createGuest(guest: Guest): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Guest_Name, guest.name)
            put(TOTAL_FAMILY_MEMBERS, guest.totalFamilyMembers)
            put(NOTE, guest.note)
            put(GUEST_STATUS, guest.isInvitationSent)
            put(GUEST_CONTACT, guest.phoneNumber)
            put(GUEST_Acceptence_Status, guest.Acceptence)
            put(GUEST_ADDRESS, guest.address)
        }
        val id = db.insert(TABLE_GUEST, null, values)
        db.close()
        return id
    }

    // Get all guests
    @SuppressLint("Range")
    fun getAllGuests(): MutableList<Guest> {
        val guests = ArrayList<Guest>()
        val selectQuery = "SELECT * FROM $TABLE_GUEST"
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery(selectQuery, null)
        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val id = it.getLong(it.getColumnIndex(COLUMN_ID))
                    val name = it.getString(it.getColumnIndex(Guest_Name))
                    val totalFamilyMembers = it.getString(it.getColumnIndex(TOTAL_FAMILY_MEMBERS))
                    val note = it.getString(it.getColumnIndex(NOTE))
                    val status = it.getString(it.getColumnIndex(GUEST_STATUS))
                    val contact = it.getString(it.getColumnIndex(GUEST_CONTACT))
                    val Acceptence = it.getString(it.getColumnIndex(GUEST_Acceptence_Status))
                    val address = it.getString(it.getColumnIndex(GUEST_ADDRESS))
                    val guest = Guest(id, name, totalFamilyMembers, note, status, contact,Acceptence, address)
                    guests.add(guest)
                } while (it.moveToNext())
            }
        }
        cursor?.close()
        db.close()
        return guests
    }
    //Search Guest
    @SuppressLint("Range")
    fun SearchGuest(query:String):MutableList<Guest>{
        val db=readableDatabase
        val cursor=db.query(
            TABLE_GUEST,
            null,
            "$Guest_Name LIKE ?",
            arrayOf("%$query%"),
            null,
            null,
            null
        )
        val GuestList= mutableListOf<Guest>()
        if(cursor.moveToFirst()){
            do {
                val Guestid=cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
                val guestName = cursor.getString(cursor.getColumnIndex(Guest_Name))
                val totalFamilyMembers = cursor.getString(cursor.getColumnIndex(TOTAL_FAMILY_MEMBERS))
                val guestNote = cursor.getString(cursor.getColumnIndex(NOTE))
                val guestStatus = cursor.getString(cursor.getColumnIndex(GUEST_STATUS))
                val guestContact = cursor.getString(cursor.getColumnIndex(GUEST_CONTACT))
                val Acceptence = cursor.getString(cursor.getColumnIndex(GUEST_Acceptence_Status))
                val guestAddress = cursor.getString(cursor.getColumnIndex(GUEST_ADDRESS))

                GuestList.add(Guest(Guestid, guestName, totalFamilyMembers, guestNote, guestStatus, guestContact, Acceptence,guestAddress))
            }while (cursor.moveToNext())
        }
        db.close()
        return GuestList
    }

    fun getTotalInvitationsSent(): Int {
        var total = 0
        val query = "SELECT COUNT(*) FROM $TABLE_GUEST WHERE $GUEST_STATUS = 'Invitation Sent'"
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery(query, null)
        cursor?.let {
            if (it.moveToFirst()) {
                total = it.getInt(0)
            }
        }
        cursor?.close()
        db.close()
        return total
    }
    fun getTotalInvitation():Int{
        var total=0
        val db=readableDatabase
        val query="SELECT COUNT(*) FROM $TABLE_GUEST"
        val cursor=db.rawQuery(query,null)
        cursor?.let {
            if(cursor.moveToFirst()){
                total=it.getInt(0)
            }
        }
        cursor?.close()
        db.close()
        return total
    }

    fun getTotalInvitationsNotSent(): Int {
        var total = 0
        val query = "SELECT COUNT(*) FROM $TABLE_GUEST WHERE $GUEST_STATUS = 'Not Sent'"
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery(query, null)
        cursor?.let {
            if (it.moveToFirst()) {
                total = it.getInt(0)
            }
        }
        cursor?.close()
        db.close()
        return total
    }

    @SuppressLint("Range")
    fun isInvitationsent(Guestid:Long):Boolean{
        val db=readableDatabase
        val query="SELECT $GUEST_STATUS FROM $TABLE_GUEST WHERE $COLUMN_ID = ?"
        val cursor=db.rawQuery(query, arrayOf(Guestid.toString()))
        var isinvitationsent=false
        if(cursor.moveToFirst()){
            val invitationsent=cursor.getString(cursor.getColumnIndex(GUEST_STATUS))
            if(invitationsent=="Invitation Sent"){
                isinvitationsent=true
            }
        }
        cursor.close()
        db.close()
        return isinvitationsent
    }

    // Update a guest
    fun updateGuest(guest: Guest): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Guest_Name, guest.name)
            put(TOTAL_FAMILY_MEMBERS, guest.totalFamilyMembers)
            put(NOTE, guest.note)
            put(GUEST_STATUS, guest.isInvitationSent)
            put(GUEST_CONTACT, guest.phoneNumber)
            put(GUEST_Acceptence_Status, guest.Acceptence)
            put(GUEST_ADDRESS, guest.address)
        }
        val rowsAffected = db.update(
            TABLE_GUEST,
            values,
            "$COLUMN_ID = ?",
            arrayOf(guest.id.toString())
        )
        db.close()
        return rowsAffected
    }

    fun updateGuestInvitation(id:Long,newvalue:String):Int{
        val db=writableDatabase
        val value=ContentValues().apply {
            put(GUEST_STATUS,newvalue)
        }
        val rowaffected=db.update(TABLE_GUEST,value,"$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return rowaffected
    }
    // Delete a guest
    fun deleteGuest(guest: Guest): Int {
        val db = writableDatabase
        val rowsAffected = db.delete(
            TABLE_GUEST,
            "$COLUMN_ID = ?",
            arrayOf(guest.id.toString())
        )
        db.close()
        return rowsAffected
    }

    // Create new vendor
    fun createVendor(vendor: Vendor): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Vendor_Name, vendor.name)
            put(Vendor_Category, vendor.category)
            put(Vendor_Note, vendor.note)
            put(Vendor_Estimated, vendor.estimatedAmount)
            put(Vendor_Balance, vendor.balance)
            put(Vendor_Pending, vendor.remaining)
            put(Vendor_Paid, vendor.paid)
            put(Vendor_PhoneNumber, vendor.phonenumber)
            put(Vendor_EmailId, vendor.emailid)
            put(Vendor_Website, vendor.website)
            put(Vendor_Address, vendor.address)
        }
        val id = db.insert(TABLE_VENDOR, null, values)
        db.close()
        return id
    }

    // Get all vendors

    @SuppressLint("Range")
    fun getAllVendors(): MutableList<Vendor> {
        val vendors = ArrayList<Vendor>()
        val selectQuery = "SELECT * FROM $TABLE_VENDOR"
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery(selectQuery, null)
        cursor?.let {
            if (it.moveToFirst()) {
                do {
                    val id = it.getLong(it.getColumnIndex(COLUMN_ID))
                    val name = it.getString(it.getColumnIndex(Vendor_Name))
                    val category = it.getString(it.getColumnIndex(Vendor_Category))
                    val note = it.getString(it.getColumnIndex(Vendor_Note))
                    val estimated = it.getString(it.getColumnIndex(Vendor_Estimated))
                    val pending = it.getString(it.getColumnIndex(Vendor_Pending))
                    val paid = it.getString(it.getColumnIndex(Vendor_Paid))
                    val phoneNumber = it.getString(it.getColumnIndex(Vendor_PhoneNumber))
                    val emailId = it.getString(it.getColumnIndex(Vendor_EmailId))
                    val website = it.getString(it.getColumnIndex(Vendor_Website))
                    val Address = cursor.getString(cursor.getColumnIndex(Vendor_Address))
                    val vendor = Vendor(id, name, category, note, estimated, " ", pending, paid, phoneNumber, emailId, website,Address)
                    vendors.add(vendor)
                } while (it.moveToNext())
            }
        }
        cursor?.close()
        db.close()
        return vendors
    }
    @SuppressLint("Range")
    fun SearchVendor(query: String):MutableList<Vendor>{
        val db=readableDatabase
        val cursor=db.query(
            TABLE_VENDOR,
            null,
            "$Vendor_Name LIKE ?",
            arrayOf("%$query%"),
            null,
            null,
            null
        )
        val VendorList= mutableListOf<Vendor>()
        if(cursor.moveToFirst()){
            do {
                val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndex(Vendor_Name))
                val category = cursor.getString(cursor.getColumnIndex(Vendor_Category))
                val note = cursor.getString(cursor.getColumnIndex(Vendor_Note))
                val estimated = cursor.getString(cursor.getColumnIndex(Vendor_Estimated))
                val balance = cursor.getString(cursor.getColumnIndex(Vendor_Balance))
                val pending = cursor.getString(cursor.getColumnIndex(Vendor_Pending))
                val paid = cursor.getString(cursor.getColumnIndex(Vendor_Paid))
                val phoneNumber = cursor.getString(cursor.getColumnIndex(Vendor_PhoneNumber))
                val emailId = cursor.getString(cursor.getColumnIndex(Vendor_EmailId))
                val website = cursor.getString(cursor.getColumnIndex(Vendor_Website))
                val Address = cursor.getString(cursor.getColumnIndex(Vendor_Address))
              VendorList.add(Vendor(id, name, category, note, estimated, balance, pending, paid, phoneNumber, emailId, website,Address))
            }while (cursor.moveToNext())
        }
        db.close()
        return VendorList
    }
    fun GetTotalVendorBudget():Double{
        var totalBudget=0.0
        val db=readableDatabase
        val query="SELECT SUM(CAST(${Vendor_Estimated} AS REAL)) FROM $TABLE_VENDOR "
        val cursor=db.rawQuery(query,null)
        cursor?.let {
            if(it.moveToFirst()){
                totalBudget = it.getDouble(0)
            }
        }
        cursor?.close()
        db.close()
        return totalBudget
    }
    fun GetVendorPaidAmount():Double{
        var total=0.0
        val db=readableDatabase
        val query="SELECT SUM(CAST(${Vendor_Estimated} AS REAL )) FROM $TABLE_VENDOR WHERE $Vendor_Paid= 'Paid'"
        val cursor=db.rawQuery(query,null)
        cursor?.let {
            if(it.moveToFirst()){
                total=it.getDouble(0)
            }
        }
        cursor?.close()
        db.close()
        return total
    }
    fun GetVendorNotPaidAmount():Double{
        var total=0.0
        val db=readableDatabase
        val query="SELECT SUM(CAST(${Vendor_Estimated} AS REAL)) FROM $TABLE_VENDOR WHERE $Vendor_Paid='Not Paid'"
        val cursor=db.rawQuery(query,null)
        cursor?.let {
            if(it.moveToFirst()){
                total=it.getDouble(0)
            }
        }
        cursor?.close()
        db.close()
        return total
    }

    @SuppressLint("Range")
    fun isVendorPaid(VendorId: Long): Boolean {
        val db = readableDatabase
        val query = "SELECT $Vendor_Paid FROM $TABLE_VENDOR WHERE $COLUMN_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(VendorId.toString()))
        var isPaid = false

        if (cursor.moveToFirst()) {
            val paidValue = cursor.getString(cursor.getColumnIndex(Vendor_Paid))
            if (paidValue == "Paid") {
                isPaid = true
            }
        }
        cursor.close()
        db.close()
        return isPaid
    }
    fun updateVendorPaid(id:Long,newvalue:String):Int{
        val db=writableDatabase
        val value=ContentValues().apply {
            put(Vendor_Paid,newvalue)
        }
        val rowaffected=db.update(TABLE_VENDOR,value,"$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return rowaffected
    }

    // Update a vendor
    fun updateVendor(vendor: Vendor): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Vendor_Name, vendor.name)
            put(Vendor_Category, vendor.category)
            put(Vendor_Note, vendor.note)
            put(Vendor_Estimated, vendor.estimatedAmount)
            put(Vendor_Balance, vendor.balance)
            put(Vendor_Pending, vendor.remaining)
            put(Vendor_Paid, vendor.paid)
            put(Vendor_PhoneNumber, vendor.phonenumber)
            put(Vendor_EmailId, vendor.emailid)
            put(Vendor_Website, vendor.website)
            put(Vendor_Address, vendor.address)
        }
        val rowsAffected = db.update(
            TABLE_VENDOR,
            values,
            "$COLUMN_ID = ?",
            arrayOf(vendor.id.toString())
        )
        db.close()
        return rowsAffected
    }

    // Delete a vendor
    fun deleteVendor(vendor: Vendor): Int {
        val db = writableDatabase
        val rowsAffected = db.delete(
            TABLE_VENDOR,
            "$COLUMN_ID = ?",
            arrayOf(vendor.id.toString())
        )
        db.close()
        return rowsAffected
    }
}