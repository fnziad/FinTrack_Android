package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BentoCardBorder
import com.example.ui.theme.CoralExpense
import com.example.ui.theme.EmeraldGreen

@Composable
fun DoughnutChart(
    spentAmount: Double,
    remainingAmount: Double,
    currencySymbol: String,
    modifier: Modifier = Modifier
) {
    val total = (spentAmount + remainingAmount).coerceAtLeast(1.0)
    val spentRatio = (spentAmount / total).toFloat().coerceIn(0f, 1f)
    val remainingRatio = (1f - spentRatio).coerceIn(0f, 1f)

    val animatedSpentAngle by animateFloatAsState(
        targetValue = spentRatio * 360f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "spentAngle"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, BentoCardBorder, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Budget Allocation",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(200.dp)
            ) {
                val remainingColor = EmeraldGreen
                val spentColor = CoralExpense
                val strokeWidthDp = 28.dp

                Canvas(modifier = Modifier.size(180.dp)) {
                    val strokeWidth = strokeWidthDp.toPx()

                    // Remaining background arc
                    drawArc(
                        color = remainingColor,
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth)
                    )

                    // Spent arc overlay
                    if (animatedSpentAngle > 0) {
                        drawArc(
                            color = spentColor,
                            startAngle = -90f,
                            sweepAngle = animatedSpentAngle,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }
                }

                // Center Text
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Spent",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$currencySymbol${spentAmount.toInt()}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${(spentRatio * 100).toInt()}% of budget",
                        style = MaterialTheme.typography.labelSmall,
                        color = CoralExpense,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Chart Legends
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendItem(
                    color = EmeraldGreen,
                    label = "Remaining",
                    value = "$currencySymbol${remainingAmount.coerceAtLeast(0.0).toInt()} (${(remainingRatio * 100).toInt()}%)"
                )
                LegendItem(
                    color = CoralExpense,
                    label = "Spent",
                    value = "$currencySymbol${spentAmount.toInt()} (${(spentRatio * 100).toInt()}%)"
                )
            }
        }
    }
}

@Composable
private fun LegendItem(
    color: Color,
    label: String,
    value: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
