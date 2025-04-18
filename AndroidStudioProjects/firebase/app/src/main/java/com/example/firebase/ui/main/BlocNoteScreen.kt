package com.example.firebase.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlocNoteScreen(navController: NavController) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    if (currentUser == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Veuillez vous connecter pour utiliser le bloc-notes.")
        }
        return
    }

    data class Note(var id: String = "", var content: String = "")

    val database = FirebaseDatabase.getInstance().getReference("notes/${currentUser.uid}")
    var noteText by remember { mutableStateOf("") }
    var notesList by remember { mutableStateOf(listOf<Note>()) }

    LaunchedEffect(Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val notes = snapshot.children.mapNotNull { it.getValue(Note::class.java) }
                notesList = notes
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Bloc-notes Firebase") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = noteText,
                onValueChange = { noteText = it },
                label = { Text("Nouvelle note") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Button(onClick = {
                val id = database.push().key ?: return@Button
                val note = Note(id, noteText)
                database.child(id).setValue(note)
                noteText = ""
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Ajouter la note")
            }

            Spacer(Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(notesList) { note ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(note.content)
                            IconButton(onClick = { database.child(note.id).removeValue() }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Supprimer")
                            }
                        }
                    }
                }

            }
        }
        Button(onClick = { navController.navigate("home") }) {
            Text("Retour")
        }
    }
}
