package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AmberTertiary
import com.example.ui.theme.BentoCardBorder
import com.example.ui.theme.CoralExpense
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.TealSecondary

import androidx.compose.foundation.clickable
import androidx.compose.ui.text.style.TextOverflow

data class CategorySpendItem(
    val name: String,
    val amount: Double,
    val color: Color
)

@Composable
fun CategoryBarChart(
    title: String,
    items: List<CategorySpendItem>,
    currencySymbol: String,
    onCategoryClick: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (items.isEmpty()) {
                Text(
                    text = "No category expense recorded yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            } else {
                val maxAmount = items.maxOfOrNull { it.amount }?.coerceAtLeast(1.0) ?: 1.0

                items.take(6).forEach { item ->
                    val progress = (item.amount / maxAmount).toFloat().coerceIn(0.05f, 1f)
                    val animatedProgress by animateFloatAsState(
                        targetValue = progress,
                        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
                        label = "categoryProgress"
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .then(
                                if (onCategoryClick != null) {
                                    Modifier.clickable { onCategoryClick(item.name) }
                                } else Modifier
                            )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f).padding(end = 8.dp)
                            )
                            Text(
                                text = "$currencySymbol${item.amount.toInt()}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = item.color,
                                maxLines = 1
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(animatedProgress)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(item.color)
                            )
                        }
                    }
                }
            }
        }
    }
}

val DefaultCategoryColors = listOf(
    CoralExpense,
    TealSecondary,
    AmberTertiary,
    EmeraldGreen,
    Color(0xFF8B5CF6), // Purple
    Color(0xFFEC4899)  // Pink
)
