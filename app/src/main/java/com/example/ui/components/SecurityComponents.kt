package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.security.RiskLevel
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceElevated
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.SecurityHighRisk
import com.example.ui.theme.SecurityHighRiskGlow
import com.example.ui.theme.SecuritySafe
import com.example.ui.theme.SecuritySafeGlow
import com.example.ui.theme.SecuritySuspicious
import com.example.ui.theme.SecuritySuspiciousGlow
import com.example.ui.theme.SecurityUnknown
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    borderColor: Color = CyberBorder,
    shape: RoundedCornerShape = RoundedCornerShape(18.dp),
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .border(BorderStroke(1.dp, borderColor), shape),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = CyberSurface.copy(alpha = 0.85f)
        )
    ) {
        content()
    }
}

@Composable
fun RiskBadge(
    riskLevel: RiskLevel,
    modifier: Modifier = Modifier,
    isUrdu: Boolean = false
) {
    val (color, glow, icon) = when (riskLevel) {
        RiskLevel.SAFE -> Triple(SecuritySafe, SecuritySafeGlow, Icons.Default.CheckCircle)
        RiskLevel.SUSPICIOUS -> Triple(SecuritySuspicious, SecuritySuspiciousGlow, Icons.Default.Warning)
        RiskLevel.HIGH_RISK -> Triple(SecurityHighRisk, SecurityHighRiskGlow, Icons.Default.Shield)
        RiskLevel.UNKNOWN -> Triple(SecurityUnknown, SecurityUnknown.copy(alpha = 0.2f), Icons.Default.Help)
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(glow)
            .border(1.dp, color, RoundedCornerShape(24.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = riskLevel.getDisplayName(isUrdu),
            color = color,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SecurityScoreMeter(
    riskScore: Int,
    riskLevel: RiskLevel,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = (100 - riskScore) / 100f,
        animationSpec = tween(durationMillis = 1000),
        label = "score_meter"
    )

    val color = when (riskLevel) {
        RiskLevel.SAFE -> SecuritySafe
        RiskLevel.SUSPICIOUS -> SecuritySuspicious
        RiskLevel.HIGH_RISK -> SecurityHighRisk
        RiskLevel.UNKNOWN -> SecurityUnknown
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Safety Confidence",
                fontSize = 13.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${(animatedProgress * 100).toInt()}%",
                fontSize = 15.sp,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = CyberSurfaceElevated
        )
    }
}

@Composable
fun DetectedSignalRow(
    signal: String,
    modifier: Modifier = Modifier
) {
    val isNegative = signal.contains("Insecure", ignoreCase = true) ||
            signal.contains("Typosquatting", ignoreCase = true) ||
            signal.contains("executable", ignoreCase = true) ||
            signal.contains("phishing", ignoreCase = true) ||
            signal.contains("shortener", ignoreCase = true) ||
            signal.contains("Direct numeric", ignoreCase = true)

    val iconColor = if (isNegative) SecurityHighRisk else SecuritySafe

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(8.dp)
                .clip(CircleShape)
                .background(iconColor)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = signal,
            fontSize = 13.sp,
            color = if (isNegative) TextPrimary else TextSecondary,
            lineHeight = 18.sp
        )
    }
}
