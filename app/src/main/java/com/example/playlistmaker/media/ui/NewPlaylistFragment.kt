package com.example.playlistmaker.media.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.NewPlaylistFragmentBinding
import com.example.playlistmaker.media.presentation.PlaylistsViewModel
import com.example.playlistmaker.media.domain.model.Playlist
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewPlaylistFragment : Fragment() {

    private lateinit var binding: NewPlaylistFragmentBinding
    lateinit var confirmDialog: MaterialAlertDialogBuilder
    private val playListViewModel by viewModel<PlaylistsViewModel>()
    private lateinit var playlists: ArrayList<Playlist>
    private var currentCoverPath: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = NewPlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var name = ""
        playlists = ArrayList()
        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Все несохраненные данные будут потеряны")
            .setNeutralButton("Отмена") { dialog, which ->
            }.setPositiveButton("Завершить") { dialog, which ->
                findNavController().popBackStack(R.id.mediaFragment, false)
            }
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack(R.id.mediaFragment, false)
        }
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isNotEmpty() == true) {
                    name = s.toString()
                    binding.btnCreate.isEnabled = true
                    binding.buttonBack.setOnClickListener {
                        confirmDialog.show()
                    }
                } else {
                    binding.btnCreate.isEnabled = false
                }
                binding.inputTitle1.refreshDrawableState()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        binding.inputTitle.addTextChangedListener(simpleTextWatcher)
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                //обрабатываем событие выбора пользователем фотографии
                if (uri != null) {
                    binding.addCover.setImageURI(uri)
                    saveImageToPrivateStorage(uri)
                    binding.buttonBack.setOnClickListener {
                        confirmDialog.show()
                    }
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        binding.addCover.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.btnCreate.setOnClickListener{
            playListViewModel.createPlaylist(name, binding.inputDescription.text.toString(),currentCoverPath)
            Toast.makeText(requireContext(), "Плейлист $name создан", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack(R.id.mediaFragment, false)
        }
    }
    private fun saveImageToPrivateStorage(uri: Uri) {

        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "cover_$timestamp.jpg"
        //создаём экземпляр класса File, который указывает на нужный каталог
        val filePath = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_covers")
        //создаем каталог, если он не создан
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        //создаём экземпляр класса File, который указывает на файл внутри каталога
        val file = File(filePath, fileName)
        // создаём входящий поток байтов из выбранной картинки
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        // создаём исходящий поток байтов в созданный выше файл
        val outputStream = FileOutputStream(file)
        // записываем картинку с помощью BitmapFactory
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        currentCoverPath = file.absolutePath
    }

}

