package org.tidy.upload.presentation

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import java.io.File


@Composable
fun ReportScreen(reportViewModel: ReportViewModel = koinViewModel()) {

  //  val state =  reportViewModel.state.collectAsState()
    val state by reportViewModel.state.collectAsState()

    val context = LocalContext.current
    val pdfPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
        uri?.let {
            // Converte o URI para um arquivo temporário
            val file = uriToFile(it, context)
            // Chama o use case através do ViewModel
            reportViewModel.extractPdf(file)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
           pdfPickerLauncher.launch("application/pdf")
        }) {
            Text("Selecionar PDF")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (state) {
            is ReportState.Idle -> {
                Text("Aguardando seleção de arquivo.")
            }

            is ReportState.Loading -> {
                CircularProgressIndicator()
            }

            is ReportState.Success -> {
                val data = (state as ReportState.Success).data
                LazyColumn {
                    items(data) { matchData ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            elevation = CardDefaults.elevatedCardElevation(
                                defaultElevation = 4.dp
                            )
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("Nome: ${matchData.Nome}")
                                Text("Número do Client: ${matchData.Cliente}")
                                Text("DataComissão: ${matchData.Dt_Comissao}")
                                Text("Vencimento ${matchData.Vencto}")
                                Text("Valor Base: ${matchData.Vlr_Base}")


                            }
                        }
                    }
                }
            }

            is ReportState.Error -> {
                Text(
                    text = "Erro: ${(state as ReportState.Error).message}",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}


fun uriToFile(uri: Uri, context: Context): File {
    // Abre o InputStream a partir do ContentResolver
    val inputStream = context.contentResolver.openInputStream(uri)
        ?: throw IllegalStateException("Não foi possível abrir o InputStream do URI")

    // Cria um arquivo temporário no cache do app
    val file = File(context.cacheDir, "temp_pdf_${System.currentTimeMillis()}.pdf")

    // Copia os dados do InputStream para o arquivo
    file.outputStream().use { output ->
        inputStream.copyTo(output)
    }
    return file
}