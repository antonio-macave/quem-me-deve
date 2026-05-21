package mz.co.macave.quemmedeve.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import mz.co.macave.quemmedeve.R

data class FabMenuItem(
    @StringRes val name: Int,
    @DrawableRes val iconRes: Int
)

val fabMenuItems = listOf(
    FabMenuItem(R.string.debtor, R.drawable.outline_debtor_24),
    FabMenuItem(R.string.debt, R.drawable.outline_money_24)
)