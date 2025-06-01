package com.example.mobile_dev

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun MainApp(){
    val navContorller = rememberNavController()
    MainScreen(navContorller)
}
@Composable
fun MainScreen(navController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = {
            BottomBar(currentRoute = "Home") { route ->
                // Handle navigation here
            }
        }
    ) { innerPadding ->
        HomeScreen(
            userName = "Dean Lewis",
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun HomeScreen(userName: String = "Dean Lewis", modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FC))
            .padding(16.dp)
    ) {
        // Greeting
        Text("Hello!", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(userName, fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        // Task Progress Card
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF567DF4)),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Your today's task\nalmost done!", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { /* View Task */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("View Task", color = Color(0xFF567DF4))
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // In Progress
        Text("In Progress", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            TaskCard(
                title = "Office Project",
                subtitle = "Grocery app design",
                progress = 0.6f,
                color = Color(0xFFEAF3FD),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(10.dp))
            TaskCard(
                title = "Personal Project",
                subtitle = "UI redesign",
                progress = 0.4f,
                color = Color(0xFFFEEEEE),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Task Groups
        Text("Task Groups", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        TaskGroupCard("Office Project", 23, 0.7f, Color(0xFFFFD6D6))
        TaskGroupCard("Personal Project", 30, 0.52f, Color(0xFFFFEDD5))
        TaskGroupCard("Daily Study", 23, 0.87f, Color(0xFFD1E3FF))
    }
}

@Composable
fun TaskCard(title: String, subtitle: String, progress: Float, color: Color, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = modifier
            .height(120.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(subtitle, fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = progress,
                color = Color.Blue,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun TaskGroupCard(name: String, taskCount: Int, progress: Float, progressColor: Color) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(name, fontWeight = FontWeight.Bold)
                Text("$taskCount Tasks", fontSize = 12.sp, color = Color.Gray)
            }
            CircularProgressIndicator(
                progress = progress,
                color = progressColor,
                modifier = Modifier.size(40.dp),
                strokeWidth = 4.dp
            )
        }
    }
}

data class BottomNavItem(val label: String, val icon: ImageVector)

val bottomNavItems = listOf(
    BottomNavItem("Home", Icons.Default.Home),
    BottomNavItem("Calendar", Icons.Default.DateRange),
    BottomNavItem("Add", Icons.Default.Add),
    BottomNavItem("Profile", Icons.Default.Person)
)

@Composable
fun BottomBar(currentRoute: String, onItemSelected: (String) -> Unit) {
    NavigationBar {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = item.label == currentRoute,
                onClick = { onItemSelected(item.label) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}

@Preview(showBackground = true,showSystemUi = true, name = "Home Screen Preview", widthDp = 360, heightDp = 640)
@Composable
fun PreviewHomeScreen() {
    MainScreen()
}
