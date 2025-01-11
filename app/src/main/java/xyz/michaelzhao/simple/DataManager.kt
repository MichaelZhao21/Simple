package xyz.michaelzhao.simple

import android.content.Context
import android.util.Log

// Serialization format: package|label++package|label++package|label###package|label++package|label++package|label++package|label
class DataManager(context: Context) {
    private val pm = PreferencesManager(context)

    private fun loadData(): List<List<Pair<String, String>>> {
        val data = pm.getAppData() ?: ""

        if (data == "")
            return listOf()

        return data.split("###").map { list ->
            list.split("++").map { pair ->
                val s = pair.split("|")
                Pair(s[0], s[1])
            }
        }
    }

    private fun saveData(data: List<List<Pair<String, String>>>) {
        val strData = data.joinToString(separator = "###") { list ->
            list.joinToString(separator = "++") { pair ->
                "${pair.first}|${pair.second}"
            }
        }

        pm.saveAppData(strData)
    }

    fun editOrAddEntry(newData: List<Pair<String, String>>, index: Int) {
        val data = loadData().toMutableList()

        // If index == -1, that means it's a new entry
        if (index == -1) {
            data.add(newData)
        } else {
            data[index] = newData
        }

        saveData(data)
    }
}