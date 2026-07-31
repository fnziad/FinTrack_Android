@file:OptIn(org.jetbrains.compose.resources.InternalResourceApi::class)

package takakoi.shared.generated.resources

import kotlin.OptIn
import kotlin.String
import kotlin.collections.MutableMap
import org.jetbrains.compose.resources.FontResource
import org.jetbrains.compose.resources.InternalResourceApi

private object CommonMainFont0 {
  public val space_grotesk_bold: FontResource by 
      lazy { init_space_grotesk_bold() }

  public val space_grotesk_light: FontResource by 
      lazy { init_space_grotesk_light() }

  public val space_grotesk_medium: FontResource by 
      lazy { init_space_grotesk_medium() }

  public val space_grotesk_regular: FontResource by 
      lazy { init_space_grotesk_regular() }
}

@InternalResourceApi
internal fun _collectCommonMainFont0Resources(map: MutableMap<String, FontResource>) {
  map.put("space_grotesk_bold", CommonMainFont0.space_grotesk_bold)
  map.put("space_grotesk_light", CommonMainFont0.space_grotesk_light)
  map.put("space_grotesk_medium", CommonMainFont0.space_grotesk_medium)
  map.put("space_grotesk_regular", CommonMainFont0.space_grotesk_regular)
}

internal val Res.font.space_grotesk_bold: FontResource
  get() = CommonMainFont0.space_grotesk_bold

private fun init_space_grotesk_bold(): FontResource = org.jetbrains.compose.resources.FontResource(
  "font:space_grotesk_bold",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/takakoi.shared.generated.resources/font/space_grotesk_bold.ttf", -1, -1),
    )
)

internal val Res.font.space_grotesk_light: FontResource
  get() = CommonMainFont0.space_grotesk_light

private fun init_space_grotesk_light(): FontResource = org.jetbrains.compose.resources.FontResource(
  "font:space_grotesk_light",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/takakoi.shared.generated.resources/font/space_grotesk_light.ttf", -1, -1),
    )
)

internal val Res.font.space_grotesk_medium: FontResource
  get() = CommonMainFont0.space_grotesk_medium

private fun init_space_grotesk_medium(): FontResource =
    org.jetbrains.compose.resources.FontResource(
  "font:space_grotesk_medium",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/takakoi.shared.generated.resources/font/space_grotesk_medium.ttf", -1, -1),
    )
)

internal val Res.font.space_grotesk_regular: FontResource
  get() = CommonMainFont0.space_grotesk_regular

private fun init_space_grotesk_regular(): FontResource =
    org.jetbrains.compose.resources.FontResource(
  "font:space_grotesk_regular",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/takakoi.shared.generated.resources/font/space_grotesk_regular.ttf", -1, -1),
    )
)
