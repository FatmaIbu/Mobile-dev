package com.example.mobile_dev

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OnboardingScreen(onStartClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.mobile),
            contentDescription ="Onboarding illustration",
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Task Management",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "This productivity tool is designed to help you better manage your task project-wise conveniently!",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 16.sp
            )
        }

        Button(
            onClick = onStartClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(text = "Let's start")
        }
    }
}

