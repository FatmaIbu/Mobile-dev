package com.example.mobile_dev

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen() {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text("Calendar", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        DatePickerView(
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it }
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text("Selected Date: $selectedDate")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerView(selectedDate: LocalDate, onDateSelected: (LocalDate) -> Unit) {
    // Replace with a real calendar or use MaterialDatePicker from Accompanist or M3 when available.
    DatePickerDialog(
        onDismissRequest = { },
        confirmButton = {
            TextButton(onClick = { }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = { }) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(
            state = rememberDatePickerState(),
            showModeToggle = true
        )
    }
}
