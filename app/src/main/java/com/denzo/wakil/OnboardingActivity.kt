package com.denzo.wakil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

class OnboardingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.White
            ) {
                OnboardingScreen()
            }
        }
    }
}

data class OnboardingPage(
    val title: String,
    val description: String,
    val iconRes: Int
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen() {
    val pages = listOf(
        OnboardingPage("House", "Find your dream house with a spacious yard and garage.", R.drawable.ic_house),
        OnboardingPage("Apartment", "Modern apartments in the heart of the city with great views.", R.drawable.ic_apartment),
        OnboardingPage("Room", "Comfortable rooms for rent, perfect for students and professionals.", R.drawable.ic_room),
        OnboardingPage("Condo", "Luxury condominiums with premium amenities and security.", R.drawable.ic_condo),
        OnboardingPage("Townhouse", "Elegant townhouses offering the perfect blend of style and comfort.", R.drawable.ic_townhouse)
    )

    val pagerState = rememberPagerState { pages.size }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { position ->
            OnboardingPageContent(pages[position], isLastPage = position == pages.size - 1)
        }

        BottomNavigationRow(
            currentPage = pagerState.currentPage,
            pageCount = pages.size,
            onSkip = { /* Handle skip */ },
            onNext = {
                if (pagerState.currentPage < pages.size - 1) {
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                }
            }
        )
    }
}

@Composable
fun OnboardingPageContent(page: OnboardingPage, isLastPage: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Large circular blue line-art illustration placeholder
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(Color(0xFF2563EB).copy(alpha = 0.1f))
                .border(2.dp, Color(0xFF2563EB), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = page.iconRes),
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = Color(0xFF2563EB)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = page.title,
            color = Color(0xFF1E293B),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = page.description,
            color = Color(0xFF94A3B8),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        if (isLastPage) {
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = { /* Learn more action */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(24.dp),
                border = borderStroke(1.dp, Color(0xFF2563EB)),
                modifier = Modifier.height(48.dp)
            ) {
                Text(text = "Learn more", color = Color(0xFF2563EB))
            }
        }
    }
}

@Composable
fun BottomNavigationRow(
    currentPage: Int,
    pageCount: Int,
    onSkip: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = onSkip) {
            Text(text = "SKIP", color = Color(0xFF94A3B8), fontWeight = FontWeight.SemiBold)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            repeat(pageCount) { index ->
                val isActive = index == currentPage
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .height(8.dp)
                        .width(if (isActive) 24.dp else 8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isActive) Color(0xFF2563EB) else Color(0xFF94A3B8))
                )
            }
        }

        TextButton(onClick = onNext) {
            Text(text = "NEXT", color = Color(0xFF94A3B8), fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun borderStroke(width: androidx.compose.ui.unit.Dp, color: Color) = 
    androidx.compose.foundation.BorderStroke(width, color)
