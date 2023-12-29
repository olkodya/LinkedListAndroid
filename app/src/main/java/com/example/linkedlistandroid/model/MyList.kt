package com.example.linkedlistandroid.model
import java.io.*


class MyList:Serializable {

    private var head: Node? = null
    private var size = 0

    fun getSize():Int = size

    fun isEmpty(): Boolean = size == 0

    fun clear() {
        while (!isEmpty()){
            remove(0)
        }
    }
    fun add(ob: UserType) {
        var cur = head
        if (head == null) {
            head = Node(null, ob)
            size++
            return
        }
        while (cur?.getNext() != null) {
            cur = cur.getNext()
        }
        val node = Node(null, ob)
        size++
        cur?.setNext(node)
    }

    operator fun get(index: Int): Node? {
        require(!(index < 0 || index >= size)) { "Invalid index $index" }
        var cur = head
        for (i in 0..<index) {
            cur = cur?.getNext()
        }
        return cur
    }

    fun add(ob: UserType, index: Int) {
        require(!(index < 0 || index > size)) { "Invalid index $index" }
        if (index == size) {
            add(ob)
            return
        }
        var cur = head
        var prev = head
        for (i in 0..<index) {
            prev = cur
            cur = cur?.getNext()
        }
        val node = Node(null, ob)
        if (prev == cur) {
            head = node
            node.setNext(cur)
            size++
            return
        }
        size++
        prev?.setNext(node)
        node.setNext(cur)
    }

    fun remove(index: Int): UserType? {
        require(!(index < 0 || index >= size)) { "Invalid index $index" }
        var cur = head
        var prev = head
        for (i in 0..<index) {
            prev = cur
            cur = cur?.getNext()
        }
        if (cur == prev)
            head = head?.getNext()
        val ob: UserType? = cur?.getData()
        prev?.setNext(cur?.getNext())
        size--
        return ob
    }

    private fun swap(a: Node?, aPrev: Node?, b: Node?, bPrev: Node?) {
        if (a?.getNext() == b) {
            a?.setNext(b?.getNext())
            b?.setNext(a)
            if (aPrev != null)
                aPrev.setNext(b)
            else
                head = b
        } else {
            val tmp = a?.getNext()
            a?.setNext(b?.getNext())
            b?.setNext(tmp)
            if (aPrev != null)
                aPrev.setNext(b)
            else
                head = b
            if (bPrev != null)
                bPrev.setNext(a)
        }
    }

    private fun partition(start: Int, end: Int, comparator: Comparator<UserType>): Int {
        val pivot = get(end)
        var prev: Node?
        var pPrev: Node?
        var pivotPrev: Node?
        pivotPrev = if (end - 1 < 0) null else get(end - 1)
        pPrev = if (start - 1 < 0) null else get(start - 1)
        var cur = get(start)
        var pIndex = start
        var p = get(pIndex)
        prev = if (start - 1 < 0) null else get(start - 1)
        while (cur != pivot) {
            if (comparator.compare(cur?.getData(), pivot?.getData()) <= 0) {
                swap(p, pPrev, cur, prev)
                if (cur == pivotPrev) pivotPrev = p
                val tmp = p
                pPrev = cur
                p = cur?.getNext()
                pIndex++
                cur = tmp?.getNext()
                prev = tmp
            } else {
                prev = cur
                cur = cur?.getNext()
            }
        }
        swap(p, pPrev, pivot, pivotPrev)
        return pIndex
    }

    fun quickSort(start: Int, end: Int, comparator: Comparator<UserType>) {
        if (start >= end) return
        val pivot = partition(start, end, comparator)
        quickSort(start, pivot - 1, comparator)
        quickSort(pivot + 1, end, comparator)
    }

    fun printList() {
        var cur = head
        if (size == 0) {
            println("List is empty")
            return
        }
        while (cur != null) {
            print(cur.getData().toString() + " ")
            cur = cur.getNext()
        }
        println()
    }

    fun forEach(callBackInt: CallBack) {
        var cur = head
        while (cur != null) {
            callBackInt.toDo(cur.getData())
            cur = cur.getNext()
        }
    }

    override fun toString(): String {
        var string:String = ""
        var cur = head
        if (size == 0) {
            return  "List is empty"
        }
        while (cur != null) {
            string += cur.getData().toString() + " "
            cur = cur.getNext()
        }
       return string
    }
}