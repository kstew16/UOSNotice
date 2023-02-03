package com.tiamoh.uosnotice.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tiamoh.uosnotice.ui.theme.UOSMain

@Composable
fun OpenNoticesDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
) {
    // 팝업고려해봐
    val openDialog = remember { mutableStateOf(true) }
    if (openDialog.value) {
        CustomAlertDialog(onDismissRequest = { openDialog.value=false }) {
        //CustomAlertDialog(onDismissRequest = onDismissRequest) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = UOSMain)
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .padding(horizontal = 24.dp),
                    text = "다이얼로그 테스트",
                    //style = Typography.Title18R.copy(color = ColorAsset.G1)
                )
                Text(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.End)
                        .clickable {
                            onDismissRequest()
                        }
                        .padding(12.dp),
                    text = "스트링 res에서 OK",
                    //style = Typography.Body14M.copy(color = ColorAsset.Primary)
                )
            }
        }
    }
}

@Composable
fun CustomAlertDialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        content()
    }
}