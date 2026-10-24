package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SimBorder
import com.example.ui.theme.SimGreenPrimary
import com.example.ui.theme.SimRedPrimary
import com.example.ui.theme.SimTextPrimary
import com.example.ui.theme.SimTextSecondary

@Composable
fun SimsInputField(
  value: String,
  onValueChange: (String) -> Unit,
  placeholder: String,
  leadingIcon: ImageVector? = null,
  isPassword: Boolean = false,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  testTag: String = "",
  modifier: Modifier = Modifier,
  readOnly: Boolean = false,
  trailingContent: (@Composable () -> Unit)? = null
) {
  var passwordVisible by remember { mutableStateOf(false) }

  OutlinedTextField(
    value = value,
    onValueChange = onValueChange,
    placeholder = {
      Text(
        text = placeholder,
        color = SimTextSecondary,
        fontSize = 14.sp
      )
    },
    leadingIcon = leadingIcon?.let {
      {
        Icon(
          imageVector = it,
          contentDescription = null,
          tint = SimTextSecondary,
          modifier = Modifier.size(20.dp)
        )
      }
    },
    trailingIcon = if (isPassword) {
      {
        IconButton(
          onClick = { passwordVisible = !passwordVisible },
          modifier = Modifier.size(24.dp)
        ) {
          Icon(
            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
            contentDescription = if (passwordVisible) "Hide password" else "Show password",
            tint = SimTextSecondary,
            modifier = Modifier.size(20.dp)
          )
        }
      }
    } else trailingContent,
    visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
    keyboardOptions = keyboardOptions,
    singleLine = true,
    readOnly = readOnly,
    shape = RoundedCornerShape(10.dp),
    colors = OutlinedTextFieldDefaults.colors(
      focusedBorderColor = SimGreenPrimary,
      unfocusedBorderColor = SimBorder,
      focusedContainerColor = Color.White,
      unfocusedContainerColor = Color(0xFFFAFAFA),
      focusedTextColor = SimTextPrimary,
      unfocusedTextColor = SimTextPrimary
    ),
    modifier = modifier
      .fillMaxWidth()
      .testTag(testTag)
  )
}

@Composable
fun SimsDropdownField(
  label: String,
  selectedValue: String,
  options: List<String>,
  onOptionSelected: (String) -> Unit,
  leadingIcon: ImageVector? = null,
  modifier: Modifier = Modifier,
  testTag: String = ""
) {
  var expanded by remember { mutableStateOf(false) }

  Box(modifier = modifier.fillMaxWidth()) {
    OutlinedTextField(
      value = selectedValue.ifEmpty { label },
      onValueChange = {},
      readOnly = true,
      leadingIcon = leadingIcon?.let {
        {
          Icon(
            imageVector = it,
            contentDescription = null,
            tint = SimTextSecondary,
            modifier = Modifier.size(20.dp)
          )
        }
      },
      trailingIcon = {
        Icon(
          imageVector = Icons.Default.ArrowDropDown,
          contentDescription = "Dropdown",
          tint = SimTextSecondary
        )
      },
      shape = RoundedCornerShape(10.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = SimGreenPrimary,
        unfocusedBorderColor = SimBorder,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color(0xFFFAFAFA),
        focusedTextColor = if (selectedValue.isEmpty()) SimTextSecondary else SimTextPrimary,
        unfocusedTextColor = if (selectedValue.isEmpty()) SimTextSecondary else SimTextPrimary
      ),
      modifier = Modifier
        .fillMaxWidth()
        .clickable { expanded = true }
        .testTag(testTag)
    )

    // Invisible clickable overlay to reliably open dropdown menu
    Box(
      modifier = Modifier
        .matchParentSize()
        .clickable { expanded = true }
    )

    DropdownMenu(
      expanded = expanded,
      onDismissRequest = { expanded = false },
      modifier = Modifier
        .background(Color.White)
        .fillMaxWidth(0.9f)
    ) {
      options.forEach { option ->
        DropdownMenuItem(
          text = {
            Text(
              text = option,
              color = SimTextPrimary,
              fontWeight = if (option == selectedValue) FontWeight.SemiBold else FontWeight.Normal
            )
          },
          onClick = {
            onOptionSelected(option)
            expanded = false
          }
        )
      }
    }
  }
}

@Composable
fun SimsActionButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  backgroundColor: Color = SimGreenPrimary,
  contentColor: Color = Color.White,
  leadingIcon: ImageVector? = null,
  testTag: String = ""
) {
  Button(
    onClick = onClick,
    colors = ButtonDefaults.buttonColors(
      containerColor = backgroundColor,
      contentColor = contentColor
    ),
    shape = RoundedCornerShape(10.dp),
    modifier = modifier
      .fillMaxWidth()
      .height(50.dp)
      .testTag(testTag)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      if (leadingIcon != null) {
        Icon(
          imageVector = leadingIcon,
          contentDescription = null,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
      }
      Text(
        text = text,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold
      )
    }
  }
}
