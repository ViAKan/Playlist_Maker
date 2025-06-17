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
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.NewPlaylistFragmentBinding
import com.example.playlistmaker.media.presentation.PlaylistsViewModel
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.player.ui.PlayerActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val QUALITY = 30

class NewPlaylistFragment : Fragment() {

    private lateinit var binding: NewPlaylistFragmentBinding
    lateinit var confirmDialog: MaterialAlertDialogBuilder
    private val playListViewModel by viewModel<PlaylistsViewModel>()
    private lateinit var playlists: ArrayList<Playlist>
    private var currentCoverPath: String? = null
    private var isEditMode = false
    private var playlistId: Long = -1

    companion object {
        private const val PLAYLIST_ARG = "playlist_arg"

        fun newInstance(playlist: Playlist? = null): NewPlaylistFragment {
            return NewPlaylistFragment().apply {
                arguments = Bundle().apply {
                    playlist?.let { putParcelable(PLAYLIST_ARG, it) }
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = NewPlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var name = ""
        val inputTitleLayout = binding.inputTitle1
        val inputTitleEditText = binding.inputTitle
        val playlist = arguments?.getParcelable<Playlist>(PLAYLIST_ARG)
        isEditMode = playlist != null
        playlistId = playlist?.id ?: -1

        if (isEditMode && !playlist?.name.isNullOrEmpty()) {
            binding.btnCreate.isEnabled = true
        }

        if (isEditMode) {
            playlist?.let {
                binding.inputTitle.setText(it.name)
                binding.inputDescription.setText(it.description)
                currentCoverPath = it.coverPath

                it.coverPath?.let { path ->
                    try {
                        binding.addCover.setImageURI(Uri.parse(path))
                    } catch (e: Exception) {
                        Log.e("NewPlaylistFragment", "Error loading cover image", e)
                    }
                }
                binding.titleEditOr.text = getString(R.string.edit_playlist)
                binding.btnCreate.text = getString(R.string.save_playlist_edits)
            }
        } else {
            binding.titleEditOr.text = getString(R.string.new_playlist)
            binding.btnCreate.text =getString(R.string.save)
        }

        playlists = ArrayList()
        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(requireContext().getString(R.string.save_data))
            .setNeutralButton(requireContext().getString(R.string.deny)) { dialog, which ->
            }.setPositiveButton(requireContext().getString(R.string.finish)) { dialog, which ->
                findNavController().popBackStack()
                (activity as? PlayerActivity)?.fragmentCont?.visibility = View.GONE
            }
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
            (activity as? PlayerActivity)?.fragmentCont?.visibility = View.GONE
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
            val name = binding.inputTitle.text.toString()
            val description = binding.inputDescription.text.toString()

            if (isEditMode) {
                playListViewModel.updatePlaylist(playlistId, name, description, currentCoverPath)
                Toast.makeText(requireContext(), getString(R.string.playlist)+" $name "+requireContext().getString(R.string.created), Toast.LENGTH_SHORT).show()
            } else {
                playListViewModel.createPlaylist(name, description, currentCoverPath)
                Toast.makeText(requireContext(), getString(R.string.playlist)+" $name "+requireContext().getString(R.string.created), Toast.LENGTH_SHORT).show()
            }

            findNavController().popBackStack()
            (activity as? PlayerActivity)?.fragmentCont?.visibility = View.GONE
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
        inputStream?.use { inp ->
            outputStream.use { out ->
                BitmapFactory.decodeStream(inp).compress(Bitmap.CompressFormat.JPEG, QUALITY, out)
            }
        }
        currentCoverPath = file.absolutePath

    }

}

