package com.example.eventmatics.SQLiteDatabase.Dataclass.DatabaseAdapter

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.eventmatics.SQLiteDatabase.Dataclass.Budget
import com.example.eventmatics.SQLiteDatabase.Dataclass.Events
import com.example.eventmatics.SQLiteDatabase.Dataclass.Guest
import com.example.eventmatics.SQLiteDatabase.Dataclass.Task
import com.example.eventmatics.SQLiteDatabase.Dataclass.Vendor
import com.example.eventmatics.SwipeGesture.BudgetSwipeToDelete
import kotlinx.coroutines.currentCoroutineContext

class LocalDatabase(contex:Context,databasename:String):SQLiteOpenHelper(contex,databasename,null,DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 1
        // Table names
        private const val TABLE_Event = "Event"
        private const val TABLE_TASK = "Task"
        private const val TABLE_VENDOR = "Vendor"
        private const val TABLE_GUEST = "Guest"
        private const val TABLE_BUDGET = "Budget"

        // Common column names
        private const val COLUMN_ID = "id"

        //Event Column Name
        private const val Event_Name = "Event_Name"
        private const val Event_Date = "Event_Date"
        private const val Event_Time = "Event_Time"
        private const val Event_Budget = "Event_Budget"

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


        //Guest Column Name
        private const val Guest_Name = "Guest_Name"
        private const val TOTAL_FAMILY_MEMBERS = "total_family_members"
        private const val NOTE = "note"
        private const val GUEST_STATUS = "guest_status"
        private const val GUEST_CONTACT = "guest_contact"
        private const val GUEST_EMAIL = "guest_email"
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
                "$Event_Budget TEXT" +
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
                "$GUEST_EMAIL TEXT," +
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
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if(oldVersion<2){
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_Event")
            val createEventTableQuery = "CREATE TABLE $TABLE_Event (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$Event_Name TEXT," +
                    "$Event_Date TEXT," +
                    "$Event_Time TEXT," +
                    "$Event_Budget TEXT" +
                    ")"
            db?.execSQL(createEventTableQuery)
        }
                db?.execSQL("DROP TABLE IF EXISTS $TABLE_Event")

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
                    "$GUEST_EMAIL TEXT," +
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
        val selectQuery = "SELECT * FROM $TABLE_Event"
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
                    val event = Events(id,name, date, time, budget)
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
    fun getEventData(eventId: Int): Events? {
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

            event = Events( id,eventName, eventDate, eventTime, eventBudget)
        }

        cursor.close()
        return event
    }


//     Update an event
    fun updateEvent(event: Events): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Event_Name, event.name)
            put(Event_Date, event.Date)
            put(Event_Time, event.time)
            put(Event_Budget, event.budget)
        }
        val rowsAffected = db.update(
            TABLE_Event,
            values,
            "$COLUMN_ID = ?",
            arrayOf(event.id.toString())
        )
        db.close()
        return rowsAffected
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

    //Getting Specific data from Task
    @SuppressLint("Range")
    fun getTaskData(taskId: String): MutableList<Task> {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_TASK WHERE $COLUMN_ID LIKE '%$taskId%' OR $Task_Name LIKE '%$taskId%' OR $Task_Category LIKE '%$taskId%'"
        val cursor = db.rawQuery(query, null)
        val taskList = mutableListOf<Task>()

        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
            val taskName = cursor.getString(cursor.getColumnIndex(Task_Name))
            val taskCategory = cursor.getString(cursor.getColumnIndex(Task_Category))
            val taskNote = cursor.getString(cursor.getColumnIndex(Task_Note))
            val taskStatus = cursor.getString(cursor.getColumnIndex(Task_Status))
            val taskDate = cursor.getString(cursor.getColumnIndex(Task_Date))

            val task = Task(id, taskName, taskCategory, taskNote, taskStatus, taskDate)
            taskList.add(task)
        }

        cursor.close()
        return taskList
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
            arrayOf("%$query"),
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
    fun updateBudgetPaid(id:Long,newvalue:String):Int{
        val db=writableDatabase
        val value=ContentValues().apply {
            put(Budget_Paid,newvalue)
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

    // Create new guest
    fun createGuest(guest: Guest): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Guest_Name, guest.name)
            put(TOTAL_FAMILY_MEMBERS, guest.totalFamilyMembers)
            put(NOTE, guest.note)
            put(GUEST_STATUS, guest.isInvitationSent)
            put(GUEST_CONTACT, guest.phoneNumber)
            put(GUEST_EMAIL, guest.email)
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
                    val email = it.getString(it.getColumnIndex(GUEST_EMAIL))
                    val address = it.getString(it.getColumnIndex(GUEST_ADDRESS))
                    val guest = Guest(id, name, totalFamilyMembers, note, status, contact, email, address)
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
            arrayOf("%$query"),
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
                val guestEmail = cursor.getString(cursor.getColumnIndex(GUEST_EMAIL))
                val guestAddress = cursor.getString(cursor.getColumnIndex(GUEST_ADDRESS))

                GuestList.add(Guest(Guestid, guestName, totalFamilyMembers, guestNote, guestStatus, guestContact, guestEmail, guestAddress))
            }while (cursor.moveToNext())
        }
        db.close()
        return GuestList
    }

    //Getting Specific data from Guest
    @SuppressLint("Range")
    fun getGuestData(guestId: Int): Guest? {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_GUEST WHERE $COLUMN_ID = $guestId"
        val cursor = db.rawQuery(query, null)
        var guest: Guest? = null

        if (cursor.moveToFirst()) {
            val guestName = cursor.getString(cursor.getColumnIndex(Guest_Name))
            val totalFamilyMembers = cursor.getString(cursor.getColumnIndex(TOTAL_FAMILY_MEMBERS))
            val guestNote = cursor.getString(cursor.getColumnIndex(NOTE))
            val guestStatus = cursor.getString(cursor.getColumnIndex(GUEST_STATUS))
            val guestContact = cursor.getString(cursor.getColumnIndex(GUEST_CONTACT))
            val guestEmail = cursor.getString(cursor.getColumnIndex(GUEST_EMAIL))
            val guestAddress = cursor.getString(cursor.getColumnIndex(GUEST_ADDRESS))

            guest = Guest(guestId.toLong(), guestName, totalFamilyMembers, guestNote, guestStatus, guestContact, guestEmail, guestAddress)
        }

        cursor.close()
        db.close()
        return guest
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
            put(GUEST_EMAIL, guest.email)
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
            put(Budget_Paid,newvalue)
        }
        val rowaffected=db.update(TABLE_BUDGET,value,"$COLUMN_ID=?", arrayOf(id.toString()))
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
                    val balance = it.getString(it.getColumnIndex(Vendor_Balance))
                    val pending = it.getString(it.getColumnIndex(Vendor_Pending))
                    val paid = it.getString(it.getColumnIndex(Vendor_Paid))
                    val phoneNumber = it.getString(it.getColumnIndex(Vendor_PhoneNumber))
                    val emailId = it.getString(it.getColumnIndex(Vendor_EmailId))
                    val website = it.getString(it.getColumnIndex(Vendor_Website))
                    val Address = cursor.getString(cursor.getColumnIndex(Vendor_Address))
                    val vendor = Vendor(id, name, category, note, estimated, balance, pending, paid, phoneNumber, emailId, website,Address)
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
            arrayOf("%$query"),
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
    //Getting Specific Data from Vendor
    @SuppressLint("Range")
    fun getVendorData(vendorId: Int): Vendor? {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_VENDOR WHERE $COLUMN_ID = $vendorId"
        val cursor = db.rawQuery(query, null)
        var vendor: Vendor? = null

        if (cursor.moveToFirst()) {
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
            val vendor = Vendor(id, name, category, note, estimated, balance, pending, paid, phoneNumber, emailId, website,Address)
        }

        cursor.close()
        return vendor
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