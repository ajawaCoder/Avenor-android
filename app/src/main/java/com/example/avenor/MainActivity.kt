/*package com.example.avenor

 import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.avenor.ui.theme.AvenorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AvenorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AvenorTheme {
        Greeting("Android")
    }
}
*/
//app/src/main/java/com/avenor/app/MainActivity.kt

package com.example.avenor

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

private val Background = Color(0xFF08090B)
private val Surface = Color(0xFF111318)
private val Surface2 = Color(0xFF181B21)
private val Border = Color(0xFF292D35)
private val PrimaryText = Color(0xFFF5F5F5)
private val SecondaryText = Color(0xFF969BA6)
private val Accent = Color(0xFF8B9CFF)
private val Green = Color(0xFF65D6A0)
private val Red = Color(0xFFFF7373)
private val Orange = Color(0xFFFFB86B)

data class AvenorTask(
    val id: Long,
    var title: String,
    var description: String,
    var className: String,
    var dueDate: String,
    var priority: String,
    var estimatedMinutes: Int,
    var completed: Boolean
)

data class AvenorClass(
    val id: Long,
    var name: String,
    var teacher: String,
    var room: String
)

data class AvenorEvent(
    val id: Long,
    var title: String,
    var date: String,
    var startTime: String,
    var endTime: String
)

class AvenorStorage(context: Context) {

    private val preferences =
        context.getSharedPreferences("avenor_data", Context.MODE_PRIVATE)

    fun saveTasks(tasks: List<AvenorTask>) {
        val array = JSONArray()

        tasks.forEach {
            val objectData = JSONObject()

            objectData.put("id", it.id)
            objectData.put("title", it.title)
            objectData.put("description", it.description)
            objectData.put("className", it.className)
            objectData.put("dueDate", it.dueDate)
            objectData.put("priority", it.priority)
            objectData.put("estimatedMinutes", it.estimatedMinutes)
            objectData.put("completed", it.completed)

            array.put(objectData)
        }

        preferences.edit()
            .putString("tasks", array.toString())
            .apply()
    }

    fun loadTasks(): MutableList<AvenorTask> {
        val result = mutableListOf<AvenorTask>()

        val raw = preferences.getString("tasks", null) ?: return result

        try {
            val array = JSONArray(raw)

            for (i in 0 until array.length()) {
                val item = array.getJSONObject(i)

                result.add(
                    AvenorTask(
                        id = item.getLong("id"),
                        title = item.getString("title"),
                        description = item.getString("description"),
                        className = item.getString("className"),
                        dueDate = item.getString("dueDate"),
                        priority = item.getString("priority"),
                        estimatedMinutes = item.getInt("estimatedMinutes"),
                        completed = item.getBoolean("completed")
                    )
                )
            }
        } catch (_: Exception) {
        }

        return result
    }

    fun saveClasses(classes: List<AvenorClass>) {
        val array = JSONArray()

        classes.forEach {
            val objectData = JSONObject()

            objectData.put("id", it.id)
            objectData.put("name", it.name)
            objectData.put("teacher", it.teacher)
            objectData.put("room", it.room)

            array.put(objectData)
        }

        preferences.edit()
            .putString("classes", array.toString())
            .apply()
    }

    fun loadClasses(): MutableList<AvenorClass> {
        val result = mutableListOf<AvenorClass>()

        val raw = preferences.getString("classes", null) ?: return result

        try {
            val array = JSONArray(raw)

            for (i in 0 until array.length()) {
                val item = array.getJSONObject(i)

                result.add(
                    AvenorClass(
                        id = item.getLong("id"),
                        name = item.getString("name"),
                        teacher = item.getString("teacher"),
                        room = item.getString("room")
                    )
                )
            }
        } catch (_: Exception) {
        }

        return result
    }

    fun saveEvents(events: List<AvenorEvent>) {
        val array = JSONArray()

        events.forEach {
            val objectData = JSONObject()

            objectData.put("id", it.id)
            objectData.put("title", it.title)
            objectData.put("date", it.date)
            objectData.put("startTime", it.startTime)
            objectData.put("endTime", it.endTime)

            array.put(objectData)
        }

        preferences.edit()
            .putString("events", array.toString())
            .apply()
    }

    fun loadEvents(): MutableList<AvenorEvent> {
        val result = mutableListOf<AvenorEvent>()

        val raw = preferences.getString("events", null) ?: return result

        try {
            val array = JSONArray(raw)

            for (i in 0 until array.length()) {
                val item = array.getJSONObject(i)

                result.add(
                    AvenorEvent(
                        id = item.getLong("id"),
                        title = item.getString("title"),
                        date = item.getString("date"),
                        startTime = item.getString("startTime"),
                        endTime = item.getString("endTime")
                    )
                )
            }
        } catch (_: Exception) {
        }

        return result
    }
}

enum class AvenorPage {
    HOME,
    TASKS,
    CALENDAR,
    CLASSES,
    PROFILE
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AvenorApp(this)
        }
    }
}

@Composable
fun AvenorApp(context: Context) {

    val storage = remember {
        AvenorStorage(context)
    }

    var currentPage by remember {
        mutableStateOf(AvenorPage.HOME)
    }

    var tasks by remember {
        mutableStateOf(storage.loadTasks())
    }

    var classes by remember {
        mutableStateOf(storage.loadClasses())
    }

    var events by remember {
        mutableStateOf(storage.loadEvents())
    }

    MaterialTheme(
        colorScheme = darkColorScheme(
            background = Background,
            surface = Surface,
            primary = Accent
        )
    ) {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Background
        ) {

            Scaffold(
                containerColor = Background,

                bottomBar = {

                    AvenorBottomBar(
                        currentPage = currentPage,
                        onPageSelected = {
                            currentPage = it
                        }
                    )
                }

            ) { padding ->

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {

                    when (currentPage) {

                        AvenorPage.HOME -> {
                            HomeScreen(
                                tasks = tasks,
                                events = events,
                                onNavigate = {
                                    currentPage = it
                                }
                            )
                        }

                        AvenorPage.TASKS -> {
                            TasksScreen(
                                tasks = tasks,
                                classes = classes,
                                onTasksChanged = {
                                    tasks = it
                                    storage.saveTasks(it)
                                }
                            )
                        }

                        AvenorPage.CALENDAR -> {
                            CalendarScreen(
                                events = events,
                                onEventsChanged = {
                                    events = it
                                    storage.saveEvents(it)
                                }
                            )
                        }

                        AvenorPage.CLASSES -> {
                            ClassesScreen(
                                classes = classes,
                                onClassesChanged = {
                                    classes = it
                                    storage.saveClasses(it)
                                }
                            )
                        }

                        AvenorPage.PROFILE -> {
                            ProfileScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AvenorBottomBar(
    currentPage: AvenorPage,
    onPageSelected: (AvenorPage) -> Unit
) {

    NavigationBar(
        containerColor = Surface,
        tonalElevation = 0.dp
    ) {

        NavigationBarItem(
            selected = currentPage == AvenorPage.HOME,
            onClick = { onPageSelected(AvenorPage.HOME) },
            icon = {
                Icon(Icons.Default.Home, "Home")
            },
            label = {
                Text("Home")
            }
        )

        NavigationBarItem(
            selected = currentPage == AvenorPage.TASKS,
            onClick = { onPageSelected(AvenorPage.TASKS) },
            icon = {
                Icon(Icons.Default.CheckCircle, "Tasks")
            },
            label = {
                Text("Tasks")
            }
        )

        NavigationBarItem(
            selected = currentPage == AvenorPage.CALENDAR,
            onClick = { onPageSelected(AvenorPage.CALENDAR) },
            icon = {
                Icon(Icons.Default.DateRange, "Calendar")
            },
            label = {
                Text("Calendar")
            }
        )

        NavigationBarItem(
            selected = currentPage == AvenorPage.CLASSES,
            onClick = { onPageSelected(AvenorPage.CLASSES) },
            icon = {
                Icon(Icons.Default.School, "Classes")
            },
            label = {
                Text("Classes")
            }
        )

        NavigationBarItem(
            selected = currentPage == AvenorPage.PROFILE,
            onClick = { onPageSelected(AvenorPage.PROFILE) },
            icon = {
                Icon(Icons.Default.Person, "Profile")
            },
            label = {
                Text("Profile")
            }
        )
    }
}

@Composable
fun HomeScreen(
    tasks: List<AvenorTask>,
    events: List<AvenorEvent>,
    onNavigate: (AvenorPage) -> Unit
) {

    val incompleteTasks = tasks.filter { !it.completed }
    val completedTasks = tasks.count { it.completed }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(
            top = 32.dp,
            bottom = 32.dp
        ),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {

        item {

            Text(
                text = "AVENOR",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp,
                color = Accent
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Good evening.",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryText
            )

            Text(
                text = "Here's what's ahead.",
                fontSize = 16.sp,
                color = SecondaryText
            )
        }

        item {

            SectionTitle("Focus Now")

            Spacer(modifier = Modifier.height(8.dp))

            if (incompleteTasks.isEmpty()) {

                EmptyCard(
                    title = "You're all caught up.",
                    subtitle = "Enjoy the clear schedule."
                )

            } else {

                val focusTask = incompleteTasks.first()

                TaskCard(
                    task = focusTask,
                    onComplete = {}
                )
            }
        }

        item {

            SectionTitle("Today")

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                StatCard(
                    modifier = Modifier.weight(1f),
                    number = incompleteTasks.size.toString(),
                    label = "Tasks"
                )

                StatCard(
                    modifier = Modifier.weight(1f),
                    number = events.size.toString(),
                    label = "Events"
                )

                StatCard(
                    modifier = Modifier.weight(1f),
                    number = completedTasks.toString(),
                    label = "Done"
                )
            }
        }

        item {

            SectionTitle("Coming Up")

            Spacer(modifier = Modifier.height(8.dp))

            if (incompleteTasks.isEmpty()) {

                EmptyCard(
                    title = "Nothing coming up.",
                    subtitle = "Add a task to get started."
                )

            } else {

                incompleteTasks.take(4).forEach { task ->

                    SimpleTaskRow(task)

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        item {

            SectionTitle("Quick Actions")

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                QuickAction(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.CheckCircle,
                    title = "Tasks",
                    onClick = {
                        onNavigate(AvenorPage.TASKS)
                    }
                )

                QuickAction(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.DateRange,
                    title = "Calendar",
                    onClick = {
                        onNavigate(AvenorPage.CALENDAR)
                    }
                )
            }
        }
    }
}

@Composable
fun TasksScreen(
    tasks: MutableList<AvenorTask>,
    classes: List<AvenorClass>,
    onTasksChanged: (MutableList<AvenorTask>) -> Unit
) {

    var showAddTask by remember {
        mutableStateOf(false)
    }

    var selectedFilter by remember {
        mutableStateOf("All")
    }

    val filteredTasks = when (selectedFilter) {

        "Active" -> tasks.filter { !it.completed }

        "Completed" -> tasks.filter { it.completed }

        else -> tasks
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 20.dp)
    ) {

        Spacer(modifier = Modifier.height(28.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "Tasks",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryText
            )

            IconButton(
                onClick = {
                    showAddTask = true
                }
            ) {

                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Task",
                    tint = Accent
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            listOf(
                "All",
                "Active",
                "Completed"
            ).forEach { filter ->

                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = {
                        selectedFilter = filter
                    },
                    label = {
                        Text(filter)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        if (filteredTasks.isEmpty()) {

            EmptyCard(
                title = "No tasks here.",
                subtitle = "Tap + to create your first task."
            )

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 30.dp)
            ) {

                items(
                    filteredTasks,
                    key = { it.id }
                ) { task ->

                    TaskCard(
                        task = task,
                        onComplete = {

                            val updated = tasks.toMutableList()

                            val index =
                                updated.indexOfFirst {
                                    it.id == task.id
                                }

                            if (index >= 0) {
                                updated[index] =
                                    updated[index].copy(
                                        completed = !updated[index].completed
                                    )
                            }

                            onTasksChanged(updated)
                        }
                    )
                }
            }
        }
    }

    if (showAddTask) {

        AddTaskDialog(
            classes = classes,
            onDismiss = {
                showAddTask = false
            },
            onCreate = { newTask ->

                val updated = tasks.toMutableList()
                updated.add(newTask)

                onTasksChanged(updated)

                showAddTask = false
            }
        )
    }
}

@Composable
fun AddTaskDialog(
    classes: List<AvenorClass>,
    onDismiss: () -> Unit,
    onCreate: (AvenorTask) -> Unit
) {

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var className by remember {
        mutableStateOf(
            classes.firstOrNull()?.name ?: ""
        )
    }
    var dueDate by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("Medium") }
    var estimated by remember { mutableStateOf("30") }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text(
                "New Task",
                fontWeight = FontWeight.Bold
            )
        },

        text = {

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task name") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") }
                )

                OutlinedTextField(
                    value = className,
                    onValueChange = { className = it },
                    label = { Text("Class") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text("Due date") },
                    placeholder = {
                        Text("MM/DD/YYYY")
                    },
                    singleLine = true
                )

                OutlinedTextField(
                    value = priority,
                    onValueChange = { priority = it },
                    label = { Text("Priority") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = estimated,
                    onValueChange = {
                        estimated = it.filter { character ->
                            character.isDigit()
                        }
                    },
                    label = { Text("Estimated minutes") },
                    singleLine = true
                )
            }
        },

        confirmButton = {

            Button(
                onClick = {

                    if (title.isNotBlank()) {

                        onCreate(
                            AvenorTask(
                                id = System.currentTimeMillis(),
                                title = title,
                                description = description,
                                className = className,
                                dueDate = dueDate,
                                priority = priority,
                                estimatedMinutes =
                                    estimated.toIntOrNull() ?: 30,
                                completed = false
                            )
                        )
                    }
                }
            ) {
                Text("Create")
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun CalendarScreen(
    events: MutableList<AvenorEvent>,
    onEventsChanged: (MutableList<AvenorEvent>) -> Unit
) {

    var showAddEvent by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 20.dp)
    ) {

        Spacer(modifier = Modifier.height(28.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                "Calendar",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryText
            )

            IconButton(
                onClick = {
                    showAddEvent = true
                }
            ) {

                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add event",
                    tint = Accent
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = SimpleDateFormat(
                "EEEE, MMMM d",
                Locale.getDefault()
            ).format(Date()),
            fontSize = 16.sp,
            color = SecondaryText
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (events.isEmpty()) {

            EmptyCard(
                title = "Your calendar is clear.",
                subtitle = "Add an event with the + button."
            )

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(
                    events,
                    key = { it.id }
                ) { event ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Surface
                        ),
                        shape = RoundedCornerShape(18.dp)
                    ) {

                        Row(
                            modifier = Modifier.padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(48.dp)
                                    .clip(
                                        RoundedCornerShape(4.dp)
                                    )
                                    .background(Accent)
                            )

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {

                                Text(
                                    event.title,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryText
                                )

                                Text(
                                    event.date,
                                    color = SecondaryText,
                                    fontSize = 13.sp
                                )

                                Text(
                                    "${event.startTime} - ${event.endTime}",
                                    color = SecondaryText,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddEvent) {

        AddEventDialog(
            onDismiss = {
                showAddEvent = false
            },
            onCreate = {

                val updated = events.toMutableList()
                updated.add(it)

                onEventsChanged(updated)

                showAddEvent = false
            }
        )
    }
}

@Composable
fun AddEventDialog(
    onDismiss: () -> Unit,
    onCreate: (AvenorEvent) -> Unit
) {

    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var start by remember { mutableStateOf("") }
    var end by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text("New Event")
        },

        text = {

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Event") }
                )

                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date") }
                )

                OutlinedTextField(
                    value = start,
                    onValueChange = { start = it },
                    label = { Text("Start time") }
                )

                OutlinedTextField(
                    value = end,
                    onValueChange = { end = it },
                    label = { Text("End time") }
                )
            }
        },

        confirmButton = {

            Button(
                onClick = {

                    if (title.isNotBlank()) {

                        onCreate(
                            AvenorEvent(
                                id = System.currentTimeMillis(),
                                title = title,
                                date = date,
                                startTime = start,
                                endTime = end
                            )
                        )
                    }
                }
            ) {
                Text("Create")
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ClassesScreen(
    classes: MutableList<AvenorClass>,
    onClassesChanged: (MutableList<AvenorClass>) -> Unit
) {

    var showAddClass by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 20.dp)
    ) {

        Spacer(modifier = Modifier.height(28.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                "Classes",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryText
            )

            IconButton(
                onClick = {
                    showAddClass = true
                }
            ) {

                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add class",
                    tint = Accent
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        if (classes.isEmpty()) {

            EmptyCard(
                title = "No classes yet.",
                subtitle = "Add your first class."
            )

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(
                    classes,
                    key = { it.id }
                ) { schoolClass ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Surface
                        ),
                        shape = RoundedCornerShape(18.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {

                            Text(
                                schoolClass.name,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryText
                            )

                            Spacer(
                                modifier = Modifier.height(5.dp)
                            )

                            Text(
                                schoolClass.teacher,
                                color = SecondaryText
                            )

                            if (schoolClass.room.isNotBlank()) {

                                Text(
                                    "Room ${schoolClass.room}",
                                    color = SecondaryText,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddClass) {

        AddClassDialog(
            onDismiss = {
                showAddClass = false
            },
            onCreate = {

                val updated = classes.toMutableList()
                updated.add(it)

                onClassesChanged(updated)

                showAddClass = false
            }
        )
    }
}

@Composable
fun AddClassDialog(
    onDismiss: () -> Unit,
    onCreate: (AvenorClass) -> Unit
) {

    var name by remember { mutableStateOf("") }
    var teacher by remember { mutableStateOf("") }
    var room by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text("New Class")
        },

        text = {

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Class name") }
                )

                OutlinedTextField(
                    value = teacher,
                    onValueChange = { teacher = it },
                    label = { Text("Teacher") }
                )

                OutlinedTextField(
                    value = room,
                    onValueChange = { room = it },
                    label = { Text("Room") }
                )
            }
        },

        confirmButton = {

            Button(
                onClick = {

                    if (name.isNotBlank()) {

                        onCreate(
                            AvenorClass(
                                id = System.currentTimeMillis(),
                                name = name,
                                teacher = teacher,
                                room = room
                            )
                        )
                    }
                }
            ) {
                Text("Create")
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ProfileScreen() {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(
            top = 32.dp,
            bottom = 40.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {

            Text(
                "Profile",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryText
            )
        }

        item {

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Surface
                ),
                shape = RoundedCornerShape(20.dp)
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(
                        "Avenor Student",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryText
                    )

                    Text(
                        "Student",
                        color = SecondaryText
                    )
                }
            }
        }

        item {

            ProfileOption(
                icon = Icons.Default.Settings,
                title = "Preferences"
            )
        }

        item {

            ProfileOption(
                icon = Icons.Default.Notifications,
                title = "Notifications"
            )
        }

        item {

            ProfileOption(
                icon = Icons.Default.Palette,
                title = "Appearance"
            )
        }

        item {

            ProfileOption(
                icon = Icons.Default.Info,
                title = "About Avenor"
            )
        }

        item {

            Text(
                "Avenor",
                color = SecondaryText,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun ProfileOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {},
        colors = CardDefaults.cardColors(
            containerColor = Surface
        ),
        shape = RoundedCornerShape(16.dp)
    ) {

        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                icon,
                contentDescription = null,
                tint = SecondaryText
            )

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                title,
                color = PrimaryText,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = SecondaryText
            )
        }
    }
}

@Composable
fun TaskCard(
    task: AvenorTask,
    onComplete: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Surface
        ),
        shape = RoundedCornerShape(18.dp)
    ) {

        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Checkbox(
                checked = task.completed,
                onCheckedChange = {
                    onComplete()
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = task.title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryText,
                    textDecoration =
                        if (task.completed)
                            TextDecoration.LineThrough
                        else
                            TextDecoration.None
                )

                if (task.className.isNotBlank()) {

                    Text(
                        task.className,
                        color = SecondaryText,
                        fontSize = 13.sp
                    )
                }

                if (task.dueDate.isNotBlank()) {

                    Text(
                        "Due ${task.dueDate}",
                        color = SecondaryText,
                        fontSize = 13.sp
                    )
                }
            }

            PriorityBadge(task.priority)
        }
    }
}

@Composable
fun PriorityBadge(
    priority: String
) {

    val color = when (priority.lowercase()) {
        "high" -> Red
        "medium" -> Orange
        else -> Green
    }

    Surface(
        color = color.copy(alpha = 0.12f),
        shape = RoundedCornerShape(10.dp)
    ) {

        Text(
            priority,
            modifier = Modifier.padding(
                horizontal = 9.dp,
                vertical = 5.dp
            ),
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SimpleTaskRow(
    task: AvenorTask
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Surface
        ),
        shape = RoundedCornerShape(15.dp)
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Accent)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {

                Text(
                    task.title,
                    fontWeight = FontWeight.Medium,
                    color = PrimaryText
                )

                Text(
                    task.dueDate.ifBlank {
                        "No due date"
                    },
                    color = SecondaryText,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun SectionTitle(
    text: String
) {

    Text(
        text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = PrimaryText
    )
}

@Composable
fun StatCard(
    modifier: Modifier,
    number: String,
    label: String
) {

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Surface
        ),
        shape = RoundedCornerShape(16.dp)
    ) {

        Column(
            modifier = Modifier.padding(15.dp)
        ) {

            Text(
                number,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryText
            )

            Text(
                label,
                fontSize = 12.sp,
                color = SecondaryText
            )
        }
    }
}

@Composable
fun QuickAction(
    modifier: Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
) {

    Card(
        modifier = modifier.clickable {
            onClick()
        },
        colors = CardDefaults.cardColors(
            containerColor = Surface
        ),
        shape = RoundedCornerShape(16.dp)
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                icon,
                contentDescription = null,
                tint = Accent
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                title,
                color = PrimaryText
            )
        }
    }
}

@Composable
fun EmptyCard(
    title: String,
    subtitle: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Surface
        ),
        shape = RoundedCornerShape(18.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Green,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                title,
                fontWeight = FontWeight.Bold,
                color = PrimaryText
            )

            Text(
                subtitle,
                color = SecondaryText,
                fontSize = 13.sp
            )
        }
    }
}
