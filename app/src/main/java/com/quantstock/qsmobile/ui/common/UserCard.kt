package com.quantstock.qsmobile.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.quantstock.qsmobile.R
import com.quantstock.qsmobile.api.UserResponse

// this composable is for displaying users' info in a nice, tile-shaped format
// mostly follows the ItemCard's logic and implementation
@Composable
fun UserCard(
    user: UserResponse,
    avatarUrl: String? = null,
    onClick: (UserResponse) -> Unit,
    defaultAvatarRes: Int = R.drawable.default_pfp
) {
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick(user) }
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,

    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // place avatar in top left corner
                if (avatarUrl.isNullOrEmpty()) {
                    Image(
                        painter = painterResource(id = defaultAvatarRes),
                        contentDescription = "${user.username} avatar",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    ) // resort to default avatar if user hasn't set one
                } else {
                    AsyncImage(
                        model = avatarUrl,
                        contentDescription = "${user.username} avatar",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // place name next to avatar
                Text(
                    text = user.username,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // email below the other two
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )
        }
    }
}