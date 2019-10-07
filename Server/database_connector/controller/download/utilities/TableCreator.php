<?php

namespace database_connector\controller\download\utilities;

/**
 * Utility class for the creation of HTML tables.
 */
class TableCreator
{
    /** @var int The number of columns in the table. */
    private $number_of_columns;
    /** @var string A string holding the HTML table. */
    private $table;

    /** @var bool Is the tag &lt;thead&gt; opened? */
    private $is_head_open = false;
    /** @var bool Is the tag &lt;thead&gt; closed? */
    private $is_head_closed = false;

    /**
     * TableCreator constructor.
     *
     * @param int $number_of_columns The number of columns in the table.
     */
    public function __construct(int $number_of_columns)
    {
        $this->number_of_columns = $number_of_columns;
        $this->table = "<table class='table table-striped table-bordered'>";
    }

    /**
     * Set a "super-header" to the table.
     *
     * @param array $columns The title for the columns in the super-header.
     * @param array $columns_sizes The size of the columns in the super-header (expressed in number of
     * "normal" columns).
     */
    public function set_super_header(array $columns, array $columns_sizes)
    {
        assert(count($columns) == count($columns_sizes) && array_sum($columns_sizes) == $this->number_of_columns);
        if (!$this->is_head_open && !$this->is_head_closed) {
            $this->table .= "<thead>";
            $this->is_head_open = true;
        }
        $this->table .= "<tr>";
        for ($i = 0; $i < count($columns); $i++) {
            $this->table .= "<th scope='col' colspan='" . $columns_sizes[$i] . "'>" . $columns[$i] . "</th>";
        }
        $this->table .= "</tr>";
    }

    /**
     * Set the header of the table.
     *
     * @param array $columns The columns' title for the header.
     */
    public function set_header(array $columns)
    {
        if (!$this->is_head_open && !$this->is_head_closed) {
            $this->table .= "<thead>";
            $this->is_head_open = true;
        }
        $this->add_row_to_table($columns, "th scope='col'");
    }

    /**
     * Add a row to the table.
     *
     * @param array $columns The column's data for the row.
     */
    public function add_row(array $columns)
    {
        if (!$this->is_head_closed) {
            $this->is_head_closed = true;
            $this->table .= "</thead><tbody>";
        }
        $this->add_row_to_table($columns, "td");
    }

    /**
     * Get the full table.
     *
     * @return string The HTML table source.
     */
    public function get_table(): string
    {
        return $this->table . "</tbody></table>";
    }

    /**
     * Add a generic row to the table.
     *
     * @param array $columns The columns of the row.
     * @param string $type_of_cell The type of cell ("td" or "th").
     */
    private function add_row_to_table(array $columns, string $type_of_cell)
    {
        assert(count($columns) == $this->number_of_columns);
        $this->table .= "<tr>";
        foreach ($columns as $value) {
            $this->table .= "<" . $type_of_cell . ">" . $value . "</" . $type_of_cell . ">";
        }
        $this->table .= "</tr>";
    }
}
