package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// ── Premium Neutral Base ──────────────────────────────────────────────────────
val PremiumBlack      = Color(0xFF0A0A0F)   // Near-black for dark accents
val PremiumInk        = Color(0xFF111118)   // Deep ink card
val PremiumCharcoal   = Color(0xFF1A1A27)   // Slightly lighter dark surface
val PremiumSlate      = Color(0xFF6B7280)   // Muted label text

val PremiumBg         = Color(0xFFF5F4F2)   // Warm off-white background
val PremiumSurface    = Color(0xFFFFFFFF)   // Pure white card
val PremiumBorder     = Color(0xFFEAE9E6)   // Very subtle warm border

// ── Primary Accent ─────────────────────────────────────────────────────────
val PremiumViolet     = Color(0xFF6C52EF)   // Bold violet-indigo
val PremiumVioletSoft = Color(0xFFF0EDFF)   // Soft violet tint for bg
val PremiumVioletMid  = Color(0xFFD6CFFE)   // Mid violet for containers

// ── Semantic Accent Colors ────────────────────────────────────────────────
val PremiumEmerald    = Color(0xFF00C48C)   // Positive / income green
val PremiumEmeraldBg  = Color(0xFFE6FBF4)   // Emerald tint background
val PremiumRose       = Color(0xFFFF4757)   // Expense / negative red
val PremiumRoseBg     = Color(0xFFFFEBED)   // Rose tint background
val PremiumAmber      = Color(0xFFFFA827)   // Warning / pending amber
val PremiumAmberBg    = Color(0xFFFFF5E6)   // Amber tint background

// ── Hero Dark Card (Payday) ───────────────────────────────────────────────
val PremiumHeroCard   = Color(0xFF111118)   // Deep dark hero card
val PremiumHeroAccent = Color(0xFF6C52EF)   // Violet accent on dark

// ── Legacy aliases for component compatibility ─────────────────────────────
val BentoIndigoPrimary   = PremiumViolet
val BentoIndigoDark      = Color(0xFF3D2DB5)
val BentoIndigoLight     = PremiumVioletSoft
val BentoIndigoContainer = PremiumVioletMid

val BentoSlateBg         = PremiumBg
val BentoSlateCard       = PremiumSurface
val BentoDarkCard        = PremiumInk
val BentoCardBorder      = PremiumBorder

val BentoEmerald         = PremiumEmerald
val BentoEmeraldLight    = PremiumEmeraldBg
val BentoAmber           = PremiumAmber
val BentoAmberLight      = PremiumAmberBg
val BentoRose            = PremiumRose
val BentoRoseLight       = PremiumRoseBg

// Legacy
val Emerald80        = Color(0xFFA7F3D0)
val EmeraldGreen     = PremiumEmerald
val EmeraldDark      = Color(0xFF005C40)
val TealSecondary    = Color(0xFF0D9488)
val TealLight        = Color(0xFF99F6E4)
val AmberTertiary    = PremiumAmber
val AmberLight       = PremiumAmberBg
val CoralExpense     = PremiumRose
val MintIncome       = PremiumEmerald
val SoftBackgroundLight = PremiumBg
val CardSurfaceLight    = PremiumSurface
val SoftBackgroundDark  = Color(0xFF0F172A)
val CardSurfaceDark     = Color(0xFF1E293B)
