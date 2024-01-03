package cc.fyre.kt.command

class CommandChildMap<out C: Command> : List<C> {

    private val children = arrayListOf<C>()
    private val childrenByName = hashMapOf<String,C>()

    operator fun get(key: String): C? {
        return this.childrenByName[key.lowercase()]
    }

    fun add(element: @UnsafeVariance C): Boolean {

        if (this.children.add(element)) {

            for (alias in element.aliases) {
                this.childrenByName[alias.lowercase()] = element
            }

            this.childrenByName[element.name.lowercase()] = element
            return true
        }

        return false
    }

    override val size: Int
        get() = this.children.size

    override fun get(index: Int): C {
        return this.children[index]
    }

    override fun isEmpty(): Boolean {
        return this.children.isEmpty()
    }

    override fun indexOf(element: @UnsafeVariance C): Int {
        return this.children.indexOf(element)
    }

    override fun containsAll(elements: Collection<@UnsafeVariance C>): Boolean {
        return this.children.containsAll(elements)
    }

    override fun contains(element: @UnsafeVariance C): Boolean {
        return this.children.contains(element)
    }

    override fun iterator(): Iterator<C> {
        return this.children.iterator()
    }

    override fun listIterator(): ListIterator<C> {
        return this.children.listIterator()
    }

    override fun listIterator(index: Int): ListIterator<C> {
        return this.children.listIterator(index)
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<C> {
        return this.children.subList(fromIndex,toIndex)
    }

    override fun lastIndexOf(element: @UnsafeVariance C): Int {
        return this.children.lastIndexOf(element)
    }

}