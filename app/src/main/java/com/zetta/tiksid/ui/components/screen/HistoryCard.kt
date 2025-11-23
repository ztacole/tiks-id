package com.zetta.tiksid.ui.components.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.zetta.tiksid.R
import com.zetta.tiksid.data.model.Ticket
import com.zetta.tiksid.utils.formatDateToDateTimeMinute
import com.zetta.tiksid.utils.formatRupiah
import com.zetta.tiksid.utils.shimmerLoading

@Composable
fun HistoryCard(
    ticket: Ticket,
    onCLick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable(
                enabled = true,
                onClickLabel = "Open movie detail"
            ) { onCLick(ticket.id) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SubcomposeAsyncImage(
            model = ticket.moviePoster,
            contentDescription = null,
            loading = {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .aspectRatio(2 / 3f)
                        .clip(MaterialTheme.shapes.medium)
                        .shimmerLoading()
                )
            },
            error = {
                Image(
                    painter = painterResource(id = R.drawable.error_image_placeholder),
                    contentDescription = null,
                    modifier = Modifier
                        .width(120.dp)
                        .aspectRatio(2 / 3f)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
            },
            modifier = Modifier
                .width(120.dp)
                .aspectRatio(2 / 3f)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(12.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp, max = 180.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
            Column {
                Text(
                    text = ticket.movieTitle,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = pluralStringResource(
                        R.plurals.ticket_list_text_ticket_information,
                        ticket.seats.size,
                        ticket.seats.size,
                        formatDateToDateTimeMinute(ticket.scheduleDate)
                    ),
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column {
                val seatText = ticket.seats.joinToString(" & ")
                Text(
                    text = stringResource(R.string.ticket_list_text_seat),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = seatText,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = formatRupiah(ticket.totalPrice),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ShimmerHistoryCard(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(120.dp)
                .aspectRatio(2 / 3f)
                .clip(MaterialTheme.shapes.medium)
                .shimmerLoading()
        )
        Spacer(Modifier.width(12.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp, max = 180.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(18.dp)
                        .clip(CircleShape)
                        .shimmerLoading()
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(CircleShape)
                        .shimmerLoading()
                )
            }
            Column {
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(12.dp)
                        .clip(CircleShape)
                        .shimmerLoading()
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(170.dp)
                        .height(12.dp)
                        .clip(CircleShape)
                        .shimmerLoading()
                )
            }
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(16.dp)
                    .clip(CircleShape)
                    .shimmerLoading()
            )
        }
    }
}