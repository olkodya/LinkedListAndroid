package com.example.linkedlistandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.linkedlistandroid.databinding.ActivityMainBinding
import com.example.linkedlistandroid.model.Fraction
import com.example.linkedlistandroid.model.MyInteger
import com.example.linkedlistandroid.model.MyList
import com.example.linkedlistandroid.utils.toast
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


class MainActivity : AppCompatActivity() {
    private var listInt = MyList()
    private var listFrac = MyList()

    companion object {
        const val INTEGER_LIST = "integerList"
        const val FRACTION_LIST = "fractionList"
        const val FRACTION_FILE_NAME = "fraction.out"
        const val INTEGER_FILE_NAME = "integer.out"
    }

    private fun fillList(boolean: Boolean): MyList {
        val list = MyList()
        for (i in 0 until 10) {
            if (boolean) {
                list.add(Fraction.create())
            } else {
                list.add(MyInteger.create())
            }
        }
        return list
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var useFraction = false
        if (savedInstanceState == null) {
            listInt = fillList(false)
            listFrac = fillList(true)
        } else {
            listInt = savedInstanceState.getSerializable(INTEGER_LIST) as MyList
            listFrac = savedInstanceState.getSerializable(FRACTION_LIST) as MyList
        }

        if (useFraction) {
            binding.list.text = listFrac.toString()

        } else {
            binding.list.text = listInt.toString()
        }

        binding.group.check(R.id.integer)
        binding.group.setOnCheckedChangeListener { _, i ->
            run {
                useFraction = i != R.id.integer
                if (useFraction) {
                    binding.list.text = listFrac.toString()
                } else {
                    binding.list.text = listInt.toString()
                }
            }
        }


        binding.push.setOnClickListener {
            if (useFraction) {
                try {
                    val value = binding.value.text.toString()
                    val fraction = Fraction().parseValue(value)
                    listFrac.add(fraction)
                    binding.list.text = listFrac.toString()
                } catch (ex: IllegalArgumentException) {
                    toast(ex.message.toString())
                }
            } else {
                try {
                    val value = binding.value.text.toString()
                    val elem = MyInteger().parseValue(value)
                    listInt.add(elem)
                    binding.list.text = listInt.toString()
                } catch (ex: IllegalArgumentException) {
                    toast(R.string.invalid_integer)
                }
            }
        }


        binding.pop.setOnClickListener {
            if (listFrac.isEmpty() && useFraction || listInt.isEmpty() && !useFraction) {
                toast(R.string.string_empty)
            } else
                try {
                    val position = binding.index.text.toString().toInt()
                    if (useFraction) {
                        try {
                            val fraction = listFrac.remove(position)
                            binding.list.text = listFrac.toString()
                            toast("Popped value:  ${fraction.toString()} position: $position")
                        } catch (ex: IllegalArgumentException) {
                            toast(ex.message.toString())
                        }
                    } else {
                        try {
                            val myInteger = listInt.remove(position)
                            binding.list.text = listInt.toString()
                            toast("Popped value:  ${myInteger.toString()} position: $position")
                        } catch (ex: IllegalArgumentException) {
                            toast(ex.message.toString())
                        }
                    }
                } catch (ex: IllegalArgumentException) {
                    toast(R.string.invalid_index)
                }
        }

        binding.insert.setOnClickListener {
            if (useFraction) {
                try {
                    val position = binding.index.text.toString().toInt()
                    try {
                        val value = binding.value.text.toString()
                        val fraction = Fraction().parseValue(value)
                        listFrac.add(fraction, position)
                        binding.list.text = listFrac.toString()
                        toast("Inserted value: $fraction position: $position")
                    } catch (ex: IllegalArgumentException) {
                        toast(R.string.invalid_fraction)
                    }
                } catch (ex: IllegalArgumentException) {
                    toast(R.string.invalid_index)
                }
            } else {
                try {
                    val position = binding.index.text.toString().toInt()
                    try {
                        val value = binding.value.text.toString()
                        val myInteger = MyInteger().parseValue(value)
                        listInt.add(myInteger, position)
                        binding.list.text = listInt.toString()
                        toast("Inserted value: $myInteger position: $position")
                    } catch (ex: IllegalArgumentException) {
                        toast(R.string.invalid_intInsert)
                    }
                } catch (ex: IllegalArgumentException) {
                    toast(R.string.invalid_index)
                }
            }
        }


        binding.getById.setOnClickListener {
            if (listFrac.isEmpty() && useFraction || listInt.isEmpty() && !useFraction) {
                toast(R.string.string_empty)
            } else
                try {
                    val position = binding.index.text.toString().toInt()
                    try {
                        if (useFraction) {
                            val elem = listFrac.get(position)
                            toast("Element at index $position: ${elem?.getData().toString()}")
                        } else {
                            val elem = listInt.get(position)
                            toast("Element at index $position: ${elem?.getData().toString()}")
                        }
                    } catch (ex: IllegalArgumentException) {
                        toast(ex.message.toString(), true)
                    }
                } catch (ex: IllegalArgumentException) {
                    toast(R.string.invalid_index, true)
                }
        }


        binding.sort.setOnClickListener {
            if (listFrac.isEmpty() && useFraction || listInt.isEmpty() && !useFraction) {
                toast(R.string.string_empty)
            } else {
                if (useFraction) {
                    listFrac.quickSort(0, listFrac.getSize() - 1, Fraction().typeComparator)
                    binding.list.text = listFrac.toString()

                } else {
                    listInt.quickSort(0, listInt.getSize() - 1, MyInteger().typeComparator)
                    binding.list.text = listInt.toString()
                }
                toast(R.string.successfully)
            }
        }

        binding.clear.setOnClickListener {
            if (listFrac.isEmpty() && useFraction || listInt.isEmpty() && !useFraction) {
                toast(R.string.string_empty)
            } else {
                if (useFraction) {
                    listFrac.clear()
                    binding.list.text = listFrac.toString()
                } else {
                    listInt.clear()
                    binding.list.text = listInt.toString()
                }
                toast(R.string.successfully)
            }

        }

        binding.export.setOnClickListener {
            if (listFrac.isEmpty() && useFraction || listInt.isEmpty() && !useFraction) {
                toast(R.string.string_empty)
            } else {
                if (useFraction) {
                    val fos: FileOutputStream =
                        this.openFileOutput(FRACTION_FILE_NAME, MODE_PRIVATE)
                    val os = ObjectOutputStream(fos)
                    os.writeObject(listFrac);
                    os.close();
                    fos.close()
                    toast(R.string.successfully)
                } else {
                    val fos: FileOutputStream = this.openFileOutput(INTEGER_FILE_NAME, MODE_PRIVATE)
                    val os = ObjectOutputStream(fos)
                    os.writeObject(listInt);
                    os.close();
                    fos.close()
                    toast(R.string.successfully)
                }
            }
        }

        binding.importBt.setOnClickListener {
            if (useFraction) {
                val fis: FileInputStream = this.openFileInput(FRACTION_FILE_NAME)
                val ims = ObjectInputStream(fis)
                listFrac = ims.readObject() as MyList
                ims.close()
                fis.close()
                binding.list.text = listFrac.toString()
            } else {
                val fis: FileInputStream = this.openFileInput(INTEGER_FILE_NAME)
                val ims = ObjectInputStream(fis)
                listInt = ims.readObject() as MyList
                ims.close()
                fis.close()
                binding.list.text = listInt.toString()
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(INTEGER_LIST, listInt)
        outState.putSerializable(FRACTION_LIST, listFrac)
    }
}

